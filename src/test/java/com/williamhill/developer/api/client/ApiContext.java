package com.williamhill.developer.api.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static com.williamhill.developer.configuration.Configuration.CONFIGURATION;

public class ApiContext {

    public static final String BASE_URL = CONFIGURATION.getValue("baseUrl");
    public static final String BASE_PATH = CONFIGURATION.getValue("basePath");
    private static final String JSON_CONTENT = "application/json";
    private String ticket;

    String getApiKey() {
        return CONFIGURATION.getValue("apiKey");
    }

    private String getApiSecret() {
        return CONFIGURATION.getValue("apiSecret");
    }

    private String getUserName() {
        return CONFIGURATION.getValue("username");
    }

    private String getPassword() {
        return CONFIGURATION.getValue("password");
    }

    private String getExpiredOption() {
        return CONFIGURATION.getValue("expireOption");
    }

    private String getApiTicket() {
        if (ticket != null) {
            return ticket;
        }

        SessionsApiClient client = new SessionsApiClient(this);
        ticket = client.getTicket();
        return ticket;
    }

    RequestSpecification getRequestSpec() {
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