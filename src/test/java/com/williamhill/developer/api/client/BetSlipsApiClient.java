package com.williamhill.developer.api.client;

import com.williamhill.developer.api.dto.betslip.*;
import com.williamhill.developer.api.dto.outcome.Outcome;
import com.williamhill.developer.domain.LegType;
import com.williamhill.developer.domain.PriceType;
import io.restassured.path.json.JsonPath;

import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class BetSlipsApiClient {

    private static final String BETSLIP_ENDPOINT = "/betslips/me";
    private static final String JSON_CONTENT = "application/json";
    private static final String BETSLIP_STATUS = "A";
    private static final String BETSLIP_STATUS_CHANGED = "false";
    private final ApiContext apiContext;

    public BetSlipsApiClient(ApiContext apiContext) {
        this.apiContext = apiContext;
    }

    public BetSlipResponse getBetSlip(LegType legType, Outcome outcome, PriceType priceType) {
        BetSlipRequest betSlipRequest = createBetSlipRequest(legType, outcome, priceType);
        JsonPath response =
                given().
                        spec(apiContext.getRequestSpec()).
                        contentType(JSON_CONTENT).
                        body(betSlipRequest).
                when().
                        post(BETSLIP_ENDPOINT).
                then().
                        extract().jsonPath();
        return response.getObject("whoBetslips.bet[0]", BetSlipResponse.class);
    }

    private BetSlipRequest createBetSlipRequest(LegType legType, Outcome outcome, PriceType priceType) {

        Part part = new Part(
                outcome.getId(),
                BETSLIP_STATUS,
                priceType.getValue(),
                BETSLIP_STATUS_CHANGED,
                outcome.getOdds().getLivePrice().getPriceNum(),
                outcome.getOdds().getLivePrice().getPriceDen()
        );
        Leg leg = new Leg(legType.getValue(), Arrays.asList(part));
        return new BetSlipRequest(Arrays.asList(new WhoBetslips(Arrays.asList(leg))));
    }
}
