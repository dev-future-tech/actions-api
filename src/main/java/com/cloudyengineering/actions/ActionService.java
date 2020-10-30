package com.cloudyengineering.actions;

import io.agroal.api.AgroalDataSource;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.*;
import java.util.UUID;

@ApplicationScoped
public class ActionService {

    private final Logger log = LoggerFactory.getLogger(ActionService.class);

    @Inject
    AgroalDataSource dataSource;

    @Inject
    PulsarMessageBean pulsar;

    public Long createAction(String actionName) {
        log.debug("Creating action {}", actionName);
        Long actionId = -1L;
        try {
            actionId = saveAction(actionName);
        } catch (Exception e) {
            log.error("Error Saving action {}", actionName, e);
            return -1L;
        }

        try {
            Producer<ActionMessage> producer = this.pulsar.getPulsarClient()
                    .newProducer(Schema.JSON(ActionMessage.class))
                    .topic("actions-sync")
                    .producerName("create-action")
                    .create();
            String messageKey = UUID.randomUUID().toString();

            ActionMessage message = new ActionMessage();
            message.setActionId(actionId);
            message.setActionName(actionName);

            producer.newMessage()
                    .key(messageKey)
                    .value(message)
                    .send();
        } catch(PulsarClientException pse) {
            log.error("Error sending message with name {} to pulsar", pse, pse);
        } catch(Exception e) {
            log.error("Error persisting action message {} t database", e, e);
        }
        return actionId;
    }

    public Long saveAction(String actionName) throws Exception {
        long actionId = -1;

        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(true);
            PreparedStatement pstmt = con.prepareStatement("insert into actions(action_name) values(?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, actionName);
            int executed = pstmt.executeUpdate();

            if (executed > 0) {
                log.debug("Data inserted for {}", actionName);
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    log.debug("Getting generated id...");
                    actionId = rs.getLong(1);
                }
            } else {
                throw new RuntimeException("Error creating database record");
            }

        } catch(SQLException sqle) {
            throw new RuntimeException("Error inserting the action", sqle);
        }
        return actionId;
    }
}
