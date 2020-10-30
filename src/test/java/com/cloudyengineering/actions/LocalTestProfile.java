package com.cloudyengineering.actions;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.HashMap;
import java.util.Map;

public class LocalTestProfile implements QuarkusTestProfile {
    @Override
    public String getConfigProfile() {
        return "custom-profile";
    }

    @Override
    public Map<String, String> getConfigOverrides() {
        Map<String, String> toReturn = new HashMap<>();
        toReturn.put("quarkus.datasource.jdbc.driver", "org.h2.Driver");
        toReturn.put("quarkus.datasource.jdbc.url", "jdbc:h2:file:/Users/m_771466/work/git_multicloud/actions-api/testdb");
        toReturn.put("quarkus.datasource.db-kind", "h2");
        toReturn.put("quarkus.datasource.username", "SA");
        toReturn.put("quarkus.datasource.password", "letmein");
        return toReturn;
    }
}
