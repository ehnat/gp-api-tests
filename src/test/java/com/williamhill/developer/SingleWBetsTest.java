package com.williamhill.developer;

import com.williamhill.developer.api.client.AccountsApiClient;
import com.williamhill.developer.api.client.BetSlipsApiClient;
import com.williamhill.developer.api.client.BetsApiClient;
import com.williamhill.developer.api.client.CompetitionsApiClient;
import com.williamhill.developer.api.dto.Balance;
import com.williamhill.developer.api.dto.betslip.BetSlipResponse;
import com.williamhill.developer.api.dto.outcome.Outcome;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.williamhill.developer.domain.LegType.WIN;
import static com.williamhill.developer.domain.PriceType.LIVE_FIXED_PRICE;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.contains;

public class SingleWBetsTest extends BaseTestCase {

    private static final int BLOCK_SIZE = 100;
    private static final int BLOCK_NUM = 0;

    private final AccountsApiClient accountsApiClient = new AccountsApiClient(apiContext);
    private final BetsApiClient betsApiClient = new BetsApiClient(apiContext);
    private final BetSlipsApiClient betSlipsApiClient = new BetSlipsApiClient(apiContext);
    private final CompetitionsApiClient competitionsApiClient = new CompetitionsApiClient(apiContext);

    @Test
    public void testPlaceSingleBetWithMinStake() {

        Outcome anyOutcome = competitionsApiClient.getAnyOutcome();
        BetSlipResponse betSlip = betSlipsApiClient.getBetSlip(WIN, anyOutcome, LIVE_FIXED_PRICE);
        BigDecimal stake = betSlip.getMinStake();
        assertCustomerBalance(stake);
        Map<String, String> betData = collectBetData(anyOutcome, stake);

        Response betResponse = betsApiClient.placeBet(betData);

        assertIsBetPlaced(betResponse);
        assertIsBetInHistory(betsApiClient.getBetId(betResponse));
    }

    @Test
    public void testPlaceSingleBetWithStakeTooLow() {

        Outcome anyOutcome = competitionsApiClient.getAnyOutcome();
        BetSlipResponse betSlip = betSlipsApiClient.getBetSlip(WIN, anyOutcome, LIVE_FIXED_PRICE);
        BigDecimal stake = betSlip.getMinStake().subtract(new BigDecimal("0.01"));
        assertCustomerBalance(stake);
        Map<String, String> betData = collectBetData(anyOutcome, stake);

        Response betResponse = betsApiClient.placeBet(betData);

        assertIsBetNotPlaced(betResponse);
    }

    private void assertIsBetPlaced(Response response) {
        response.then().
                statusCode(HttpStatus.SC_CREATED).
                body("whoBets.betPlaced.betId", notNullValue());
    }

    private void assertIsBetNotPlaced(Response response) {
        response.then().
                statusCode(HttpStatus.SC_OK).
                body("whoBets.whoFaults[0].faultString", contains("Stake too low"));
    }

    private void assertCustomerBalance(BigDecimal stake) {
        Balance balance = accountsApiClient.getBalance();
        Assertions.assertThat(stake).isLessThanOrEqualTo(balance.getAvailableFunds());
    }

    private void assertIsBetInHistory(String betId) {
        Response betsHistoryResponse = betsApiClient.getBetsHistory(BLOCK_SIZE, BLOCK_NUM);
        betsHistoryResponse.then().
                statusCode(HttpStatus.SC_OK).
                body("whoBets.bet[0]", contains(betId));
    }

    private Map<String, String> collectBetData(Outcome outcome, BigDecimal stake) {
        Map<String, String> betData = new HashMap<>();
        betData.put("legType", WIN.getValue());
        betData.put("stake", stake.toString());
        betData.put("outcomeId", outcome.getId());
        betData.put("priceType", LIVE_FIXED_PRICE.getValue());
        betData.put("priceNum", outcome.getOdds().getLivePrice().getPriceNum());
        betData.put("priceDen", outcome.getOdds().getLivePrice().getPriceDen());
        return betData;
    }
}
