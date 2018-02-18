package com.williamhill.developer.apiclients;

import com.williamhill.developer.apiobjects.betslip.*;
import com.williamhill.developer.apiobjects.outcome.Outcome;
import io.restassured.path.json.JsonPath;

import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class BetSlipAPIClient {

    private ApiContext apiContext;

    private static final String BETSLIP_ENDPOINT = "/betslips/me";
    private static final String JSON_CONTENT = "application/json";
    private static final String BETSLIP_STATUS = "A";
    private static final String BETSLIP_STATUS_CHANGED = "false";

    public BetSlipAPIClient(ApiContext apiContext) {
        this.apiContext = apiContext;
    }

    public BetSlipResponse getBetSlip(String legType, Outcome outcome, String priceType) {
        BetSlipRequest betSlipRequest = createBetSlipRequest(legType, outcome, priceType);
        JsonPath response =
                given().
                        spec(apiContext.getRequestSpec()).
                        contentType(JSON_CONTENT).
                        body(betSlipRequest).
                when().
                        post(BETSLIP_ENDPOINT).
                then().log().all().
                        extract().jsonPath();
        return response.getObject("whoBetslips.bet[0]", BetSlipResponse.class);
    }

    public BetSlipRequest createBetSlipRequest(String legType, Outcome outcome, String priceType) {

        Part part = new Part(
                outcome.getId(),
                BETSLIP_STATUS,
                priceType,
                BETSLIP_STATUS_CHANGED,
                outcome.getOdds().getLivePrice().getPriceNum(),
                outcome.getOdds().getLivePrice().getPriceDen()
        );
        Leg leg = new Leg(legType, Arrays.asList(part));
        return new BetSlipRequest(Arrays.asList(new WhoBetslips(Arrays.asList(leg))));
    }
}
