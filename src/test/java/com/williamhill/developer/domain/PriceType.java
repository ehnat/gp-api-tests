package com.williamhill.developer.domain;

public enum PriceType {
    LIVE_FIXED_PRICE("L"),
    STARTING_PRICE("S"),
    GUARANTEED_BEST_PRICE("G");

    private String value;

    PriceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
