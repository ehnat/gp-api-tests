package com.williamhill.developer.api.dto.betslip;

import lombok.Getter;

@Getter
public class Part {

    private String outcomeId;
    private String status;
    private String priceType;
    private String statusChanged;
    private String priceNum;
    private String priceDen;

    public Part(String outcomeId, String status, String priceType, String statusChanged, String priceNum, String priceDen) {
        this.outcomeId = outcomeId;
        this.status = status;
        this.priceType = priceType;
        this.statusChanged = statusChanged;
        this.priceNum = priceNum;
        this.priceDen = priceDen;
    }
}
