package com.williamhill.developer.apiobjects.outcome;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class LivePrice {
    private String priceNum;
    private String priceDen;
}
