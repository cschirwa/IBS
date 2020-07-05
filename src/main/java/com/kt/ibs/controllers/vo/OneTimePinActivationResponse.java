package com.kt.ibs.controllers.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OneTimePinActivationResponse {

    private boolean success;
    private int failureCounts;
    private BeneficiaryDetails beneficiary;
}
