package com.kt.ibs.controllers.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class BeneficiariesPaymentApprovalResponse {

    private boolean success;
    private String message;
    
    private List<Transaction> transactions;
}
