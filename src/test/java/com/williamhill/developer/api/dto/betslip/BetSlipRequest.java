package com.williamhill.developer.api.dto.betslip;

import lombok.Getter;

import java.util.List;

@Getter
public class BetSlipRequest {

    private List<WhoBetslips> whoBetslips;

    public BetSlipRequest(List<WhoBetslips> whoBetslips) {
        this.whoBetslips = whoBetslips;
    }
}
