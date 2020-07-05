package com.kt.ibs.controllers.vo;

import com.kt.ibs.entity.AccountType;
import com.kt.ibs.entity.NotificationType;
import com.kt.ibs.entity.PaymentCategory;

import lombok.Getter;

@Getter
public class AddBeneficiary {

    private String beneficiaryFullname;
    private String beneficiaryReference;
    private String senderReference;
    private PaymentCategory paymentCategory;
    private AccountType accountType;
    private String accountNumber;
    private String accountName;
    private String branchCode;
    private String bankCode;
    private String currency;
    private String country;
    private NotificationType notificationType;
    private String notificationDetails;

}
