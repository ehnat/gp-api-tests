package com.williamhill.developer.api.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.URLENC;

class SessionsApiClient {

    private static final String LOGIN_ENDPOINT = "/sessions/tickets";
    private final ApiContext apiContext;

    SessionsApiClient(ApiContext apiContext) {
        this.apiContext = apiContext;
    }

    String getTicket() {
        return given().
                        spec(getLoginRequestSpec()).
                when().
                        post(LOGIN_ENDPOINT).
                then().
                        extract().path("whoSessions.ticket");
    }

    private RequestSpecification getLoginRequestSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        RequestSpecification authorization =
                given().
                        contentType(URLENC).
                        params(apiContext.getCredentials());

        builder.addRequestSpecification(authorization)
                .addHeaders(apiContext.getHeaders());
        return builder.build();
    }
}
