package com.kt.ibs.controllers.vo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BeneficiaryPayment {

    private String sourceAccountNumber;
    private String benId;
    private boolean immediate;
    private BigDecimal amount;
}
