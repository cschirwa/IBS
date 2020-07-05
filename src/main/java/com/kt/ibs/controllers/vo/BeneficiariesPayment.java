package com.kt.ibs.controllers.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BeneficiariesPayment {
private String sourceAccountNumber;
    private List<BeneficiaryPayment> payments;
}
