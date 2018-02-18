package com.williamhill.developer.apiclients;

import com.williamhill.developer.apiobjects.Balance;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;

public class AccountsAPIClient {

    private ApiContext apiContext;
    private static final String CUSTOMER_BALANCE_ENDPOINT = "/accounts/me/balance";

    public AccountsAPIClient(ApiContext apiContext) {
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
