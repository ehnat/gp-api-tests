package com.williamhill.developer.apiclients;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.URLENC;

public class SessionsAPIClient {

    private ApiContext apiContext;
    private static final String LOGIN_ENDPOINT = "/sessions/tickets";

    public SessionsAPIClient(ApiContext apiContext) {
        this.apiContext = apiContext;
    }

    public String getTicket() {
        return given().
                        spec(getLoginRequestSpec()).
                when().
                        post(LOGIN_ENDPOINT).
                then().
                        extract().path("whoSessions.ticket");
    }

    public RequestSpecification getLoginRequestSpec() {
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
