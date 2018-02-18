package com.williamhill.developer.apiobjects.betslip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class BetSlipResponse {
    private BigDecimal maxStake;
    private BigDecimal minStake;
}
