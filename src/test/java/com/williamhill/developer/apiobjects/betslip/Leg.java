package com.williamhill.developer.apiobjects.betslip;

import lombok.Getter;

import java.util.List;

@Getter
public class Leg {

    private String legType;
    private List<Part> part;

    public Leg(String legType, List<Part> part) {
        this.legType = legType;
        this.part = part;
    }
}
