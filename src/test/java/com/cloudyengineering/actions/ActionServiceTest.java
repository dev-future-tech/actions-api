package com.cloudyengineering.actions;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestProfile(LocalTestProfile.class)
public class ActionServiceTest {

    private final Logger log = LoggerFactory.getLogger(ActionServiceTest.class);

    @Inject
    ActionService service;

    @Test
    void testSaveAction() throws Exception {
        Long actionId = this.service.saveAction("my-action");

        Assertions.assertNotNull(actionId, "Action Id is null");

        log.debug("Returned id is {}", actionId);

    }
}
