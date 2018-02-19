package com.williamhill.developer.api.dto.betslip;

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
