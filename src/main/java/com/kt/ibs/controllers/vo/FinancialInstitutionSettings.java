package com.kt.ibs.controllers.vo;

import com.kt.ibs.entity.ChargeType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
@NoArgsConstructor
public class FinancialInstitutionSettings {

    private String currencyCode;
    private ChargeType chargeType;

}
