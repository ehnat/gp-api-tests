package com.williamhill.developer.apiobjects.betslip;

import lombok.Getter;

import java.util.List;

@Getter
public class BetSlipRequest {

    private List<WhoBetslips> whoBetslips;

    public BetSlipRequest(List<WhoBetslips> whoBetslips) {
        this.whoBetslips = whoBetslips;
    }
}
