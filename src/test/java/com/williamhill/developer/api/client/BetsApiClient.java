package com.williamhill.developer.api.client;

import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.URLENC;

public class BetsApiClient {

    private static final String BET_ENDPOINT = "bets/me";
    private final ApiContext apiContext;

    public BetsApiClient(ApiContext apiContext) {
        this.apiContext = apiContext;
    }

    public Response placeBet(Map<String, String> betData) {
        return
                given().
                        spec(apiContext.getRequestSpec()).
                        contentType(URLENC).
                        params(betData).
                when().
                        post(BET_ENDPOINT).
                then().
                        extract().response();
    }

    public String getBetId(Response response) {
        return
                response.then().
                        extract().path("whoBets.betPlaced.betId");
    }

    public Response getBetsHistory(int blockSize, int blockNum) {
        return
                given().
                        spec(apiContext.getRequestSpec()).
                        queryParam("blockSize", blockSize).
                        queryParam("blockNum", blockNum).
                when().
                        get(BET_ENDPOINT).
                then().
                        extract().response();
    }
}
