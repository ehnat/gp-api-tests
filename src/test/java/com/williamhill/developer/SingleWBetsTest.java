package com.williamhill.developer;

import com.williamhill.developer.apiclients.*;
import com.williamhill.developer.apiobjects.*;
import com.williamhill.developer.apiobjects.betslip.BetSlipResponse;
import com.williamhill.developer.apiobjects.outcome.Outcome;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.*;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.URLENC;
import static org.hamcrest.Matchers.*;

public class SingleWBetsTest extends BaseTestCase {

    private static final String BET_ENDPOINT = "bets/me";
    private static final String LEG_TYPE = "W";
    private static final String PRICE_TYPE = "L";
    private static final int BLOCK_SIZE = 100;
    private static final int BLOCK_NUM = 0;

    private CompetitionsApiClient competitionsApiClient = new CompetitionsApiClient(apiContext());
    private BetSlipAPIClient betSlipAPIClient = new BetSlipAPIClient(apiContext());
    private AccountsAPIClient accountsAPIClient = new AccountsAPIClient(apiContext());

    @Test
    public void testPlaceSingleBetWithMinStake() {
        Outcome anyOutcome = competitionsApiClient.getAnyOutcome();
        BetSlipResponse betSlip = betSlipAPIClient.getBetSlip(LEG_TYPE, anyOutcome, PRICE_TYPE);
        BigDecimal stake = betSlip.getMinStake();

        assertCustomerBetSlip(betSlip, stake);
        assertCustomerBalance(stake);

        Map<String, String> betsData = collectBetsData(
                LEG_TYPE, stake.toString(), anyOutcome.getId(), PRICE_TYPE,
                anyOutcome.getOdds().getLivePrice().getPriceNum(),
                anyOutcome.getOdds().getLivePrice().getPriceDen());

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
        BetSlipResponse betSlip = betSlipAPIClient.getBetSlip(LEG_TYPE, anyOutcome, PRICE_TYPE);
        BigDecimal stake = setTooLowStake(betSlip);

        assertCustomerBalance(stake);

        Map<String, String> betsData = collectBetsData(
                LEG_TYPE, stake.toString(), anyOutcome.getId(), PRICE_TYPE,
                anyOutcome.getOdds().getLivePrice().getPriceNum(),
                anyOutcome.getOdds().getLivePrice().getPriceDen());

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

    private BigDecimal setTooLowStake(BetSlipResponse betSlip) {
        BigDecimal tooLowStake = betSlip.getMinStake().min(new BigDecimal(0.01));
        if(tooLowStake.compareTo(BigDecimal.ZERO) < 0){
            return new BigDecimal(0.01);
        }
        return tooLowStake;
    }

    private void assertCustomerBalance(BigDecimal stake) {
        Balance balance = accountsAPIClient.getBalance();
        SoftAssertions sa = new SoftAssertions();
        sa.assertThat(stake)
                .isLessThanOrEqualTo(balance.getAvailableFunds())
                .isLessThanOrEqualTo(balance.getBalance())
                .isLessThanOrEqualTo(balance.getWithdrawableFunds());
        sa.assertAll();
    }

    private void assertCustomerBetSlip(BetSlipResponse betSlip, BigDecimal stake) {
        SoftAssertions sa = new SoftAssertions();
        sa.assertThat(stake)
                .isGreaterThanOrEqualTo(betSlip.getMinStake())
                .isLessThanOrEqualTo(betSlip.getMaxStake());
        sa.assertAll();
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

    private Map<String, String> collectBetsData(String legType, String stake, String outcomeId, String
            priceType, String priceNum, String priceDen) {
        Map<String, String> betData = new HashMap<>();
        betData.put("legType", legType);
        betData.put("stake", stake);
        betData.put("outcomeId", outcomeId);
        betData.put("priceType", priceType);
        betData.put("priceNum", priceNum);
        betData.put("priceDen", priceDen);
        return betData;
    }
}
