package com.williamhill.developer.api.client;

import com.williamhill.developer.api.dto.Balance;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;

public class AccountsApiClient {

    private static final String CUSTOMER_BALANCE_ENDPOINT = "/accounts/me/balance";
    private final ApiContext apiContext;

    public AccountsApiClient(ApiContext apiContext) {
        this.apiContext = apiContext;
    }

    public Balance getBalance() {
        JsonPath response =
                given().
                        spec(apiContext.getRequestSpec()).
                when().
                        get(CUSTOMER_BALANCE_ENDPOINT).
                then().
                        extract().jsonPath();

        return response.getObject("whoAccounts.account", Balance.class);
    }
}
