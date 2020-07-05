package com.kt.ibs.controllers.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class PaymentApprovalRequest {

    private String transferType;
    private String transferUUID;
    private String oneTimePin;
}
