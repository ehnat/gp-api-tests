package com.williamhill.developer.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Balance {

    private BigDecimal availableFunds;
    private BigDecimal balance;
    private BigDecimal withdrawableFunds;
    private String currencyCode;

}
