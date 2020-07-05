package com.kt.ibs.controllers.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountResponseDetails {

    private String accountNumber;
    private String customerId;
    private String category;
    private String accountTitle1;
    private String accountTitle2;
    private String shortTitle;
    private String mnemonic;
    private String positionType;
    private String currency;
    private String currencyMarket;
    private String broker;
    private String onlineBalance;
    private String actualWorkingBalance;

}