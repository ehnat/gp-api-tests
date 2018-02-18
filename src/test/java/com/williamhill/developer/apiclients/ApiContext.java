package com.williamhill.developer.apiclients;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static com.williamhill.developer.configuration.Configuration.CONFIGURATION;

public class ApiContext {

    private String ticket;
    private static final String JSON_CONTENT = "application/json";

    public ApiContext() {
    }

    String getApiKey() {
        return CONFIGURATION.getValue("apiKey");
    }

    String getApiSecret() {
        return CONFIGURATION.getValue("apiSecret");
    }

    String getUserName() {
        return CONFIGURATION.getValue("username");
    }

    String getPassword() {
        return CONFIGURATION.getValue("password");
    }

    String getExpiredOption() {
        return CONFIGURATION.getValue("expireOption");
    }

    String getApiTicket() {
        if (ticket != null) {
            return ticket;
        }

        SessionsAPIClient client = new SessionsAPIClient(this);
        ticket = client.getTicket();
        return ticket;
    }

    public RequestSpecification getRequestSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("who-ticket", getApiTicket())
                .addHeaders(getHeaders());
        return builder.build();
    }

    Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("accept", JSON_CONTENT);
        headers.put("who-apiKey", getApiKey());
        headers.put("who-secret", getApiSecret());
        return headers;
    }


    Map<String, String> getCredentials() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", getUserName());
        credentials.put("password", getPassword());
        credentials.put("extended", getExpiredOption());
        return credentials;
    }
}