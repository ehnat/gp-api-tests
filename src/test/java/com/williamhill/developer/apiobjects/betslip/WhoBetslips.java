package com.williamhill.developer.apiobjects.betslip;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class WhoBetslips {
    private List<Leg> leg = new ArrayList<>();

    public WhoBetslips(List<Leg> leg) {
        this.leg = leg;
    }
}
