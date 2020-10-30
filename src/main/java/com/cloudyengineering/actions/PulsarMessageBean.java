package com.cloudyengineering.actions;

import io.quarkus.runtime.StartupEvent;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class PulsarMessageBean {

    private final Logger log = LoggerFactory.getLogger(PulsarMessageBean.class);

    @Inject
    @ConfigProperty(name = "pulsar.service-url", defaultValue = "pulsar://localhost:6650")
    String serviceUrl;

    @Inject
    @ConfigProperty(name = "pulsar.use-tls", defaultValue = "false")
    boolean useTls;

    PulsarClient pulsarClient;

    void startup(@Observes StartupEvent event) {
        log.debug("Starting PulsarMessageBean...");
        try {
            this.pulsarClient = PulsarClient.builder()
                    .serviceUrl(this.serviceUrl)
                    .allowTlsInsecureConnection(this.useTls)
                    .build();
        } catch (PulsarClientException pcl) {
            log.error("Error starting pulsar client", pcl);
        }

    }

    public PulsarClient getPulsarClient() {
        return this.pulsarClient;
    }
}
