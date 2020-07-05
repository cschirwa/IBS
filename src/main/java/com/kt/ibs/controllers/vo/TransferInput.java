package com.kt.ibs.controllers.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferInput {

    private String sourceAccountNumber;
    private String beneficiaryAccountNumber;
    private BigDecimal transferAmount;
    private String creditCurrency;
    private String debtCurrency;
    private String fromReference;
    private String toReference;

    private String recipient;
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
    private String transferType;

}
