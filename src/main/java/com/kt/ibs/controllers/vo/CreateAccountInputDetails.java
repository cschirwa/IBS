package com.kt.ibs.controllers.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountInputDetails {

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

    private String activityName;
    private String assignReason;
    private String dueDate;
    private String exitProcess;
    private String exitProcessId;
    private String gtsControl;
    private String messageId;
    private String noOfAuth;
    private String owner;
    private String replace;
    private String startDate;
    private String user;

}