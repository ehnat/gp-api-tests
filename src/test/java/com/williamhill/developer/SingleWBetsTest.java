package com.williamhill.developer;

import com.williamhill.developer.api.client.AccountsApiClient;
import com.williamhill.developer.api.client.BetSlipApiClient;
import com.williamhill.developer.api.client.CompetitionsApiClient;
import com.williamhill.developer.api.dto.Balance;
import com.williamhill.developer.api.dto.betslip.BetSlipResponse;
import com.williamhill.developer.api.dto.outcome.Outcome;
import com.williamhill.developer.domain.LegType;
import com.williamhill.developer.domain.PriceType;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.URLENC;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.contains;

public class SingleWBetsTest extends BaseTestCase {

    private static final String BET_ENDPOINT = "bets/me";
    private static final String LEG_TYPE = LegType.WIN.getValue();
    private static final String PRICE_TYPE = PriceType.LIVE_FIXED_PRICE.getValue();
    private static final int BLOCK_SIZE = 100;
    private static final int BLOCK_NUM = 0;

    private final CompetitionsApiClient competitionsApiClient = new CompetitionsApiClient(apiContext());
    private final BetSlipApiClient betSlipApiClient = new BetSlipApiClient(apiContext());
    private final AccountsApiClient accountsApiClient = new AccountsApiClient(apiContext());

    @Test
    public void testPlaceSingleBetWithMinStake() {
        Outcome anyOutcome = competitionsApiClient.getAnyOutcome();
        BetSlipResponse betSlip = betSlipApiClient.getBetSlip(LEG_TYPE, anyOutcome, PRICE_TYPE);
        BigDecimal stake = betSlip.getMinStake();

        assertCustomerBalance(stake);

        Map<String, String> betsData = collectBetsData(anyOutcome, stake);

        String betId =
                given().
                        spec(apiContext().getRequestSpec()).
                        contentType(URLENC).
                        params(betsData).
                when().
                        post(BET_ENDPOINT).
                then().
                        statusCode(HttpStatus.SC_CREATED).
                        body("whoBets.betPlaced.betId", notNullValue()).
                        extract().path("whoBets.betPlaced.betId");

        assertIsBetInHistory(betId);
    }

    @Test
    public void testPlaceSingleBetWithStakeTooLow() {
        Outcome anyOutcome = competitionsApiClient.getAnyOutcome();
        BetSlipResponse betSlip = betSlipApiClient.getBetSlip(LEG_TYPE, anyOutcome, PRICE_TYPE);
        BigDecimal stake = betSlip.getMinStake().subtract(new BigDecimal("0.01"));

        assertCustomerBalance(stake);

        Map<String, String> betsData = collectBetsData(anyOutcome, stake);

        given().
                spec(apiContext().getRequestSpec()).
                contentType(URLENC).
                params(betsData).
        when().
                post(BET_ENDPOINT).
        then().
                statusCode(HttpStatus.SC_OK).
                body("whoBets.whoFaults[0].faultString", contains("Stake too low"));
    }

    private void assertCustomerBalance(BigDecimal stake) {
        Balance balance = accountsApiClient.getBalance();
        Assertions.assertThat(stake).isLessThanOrEqualTo(balance.getAvailableFunds());
    }

    private void assertIsBetInHistory(String betId) {
        given().
                spec(apiContext().getRequestSpec()).
                queryParam("blockSize", BLOCK_SIZE).
                queryParam("blockNum", BLOCK_NUM).
        when().
                get(BET_ENDPOINT).
        then().
                statusCode(HttpStatus.SC_OK).
                body("whoBets.bet[0]", contains(betId));
    }

    private Map<String, String> collectBetsData(Outcome outcome, BigDecimal stake) {
        Map<String, String> betData = new HashMap<>();
        betData.put("legType", LEG_TYPE);
        betData.put("stake", stake.toString());
        betData.put("outcomeId", outcome.getId());
        betData.put("priceType", PRICE_TYPE);
        betData.put("priceNum", outcome.getOdds().getLivePrice().getPriceNum());
        betData.put("priceDen", outcome.getOdds().getLivePrice().getPriceDen());
        return betData;
    }
}
