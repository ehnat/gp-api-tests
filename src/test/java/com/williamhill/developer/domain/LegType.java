package com.williamhill.developer.domain;

public enum LegType {
    WIN("W"),
    EACH_WAY("E");

    private String value;

    LegType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
