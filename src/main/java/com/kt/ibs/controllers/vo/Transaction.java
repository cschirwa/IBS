package com.kt.ibs.controllers.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.kt.ibs.entity.AccountTransaction;
import com.kt.ibs.service.Util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Transaction {

    private String accountNumber;
    private Date bookingDate;
    private Date processedDate;
    private String reference;
    private String description;
    private Date valueDate;
    private BigDecimal txnAmount;
    private BigDecimal closingBalance;
    private String sourceReference;
    private String beneficiaryReference;
    private String beneficiaryAccountNumber;
    private String beneficiaryBank;
    private String recipientText;
    private String formattedClosingBalance; 
    private String currency;

    public Transaction(final AccountTransaction entity) {
        this.accountNumber = entity.getAccount().getAccountNumber();
        this.bookingDate = entity.getBookingDate();
        this.reference = entity.getReference();
        this.description = entity.getDescription();
        this.valueDate = entity.getValueDate();
        this.txnAmount = entity.getAmount();
        this.closingBalance = entity.getClosingBalance();
        this.processedDate = entity.getCreatedTime();
        this.beneficiaryBank = entity.getBeneficiaryBank();
        if (entity.getBeneficiary() != null) {
            this.beneficiaryReference = entity.getBeneficiary().getBeneficiaryReference();
            this.recipientText = entity.getBeneficiary().getBeneficiaryFullname();
            this.beneficiaryAccountNumber = entity.getBeneficiary().getAccount().getAccountNumber();
        }
        this.sourceReference = entity.getReference();
        if(formattedClosingBalance != null) {
            formattedClosingBalance =Util.formatMoney("USD", closingBalance);
        }
    }
}
