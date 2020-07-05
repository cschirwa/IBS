package com.kt.ibs.controllers.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

import com.kt.ibs.entity.Transfer;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransferResponseDetails {
    private String transactionType;
    private String baseCurrency;
    private String paymentMethod;
    private String status;

    private String debitAccountNumber;

    private String indebtAccountNumber;

    private String currencymktdr;

    private String debitCurrency;

    private BigDecimal debitAmount;

    private Date debitValueDate;

    private Date indebitValueDate;

    private String debitTheirRef;

    private String creditTheirRef;

    private String creditAccountNumber;

    private String currencymktcr;

    private String creditCurrency;

    private BigDecimal creditAmount;

    private Date creditValueDate;

    private String treasuryRate;
    private BeneficiaryDetails beneficiaryDetails;

    private Date processingDate;
    private String coreReference;
    private Date coreProcessedTime;
    private String recipient;

    public TransferResponseDetails(final Transfer transfer) {
        this.status = transfer.getTransactionStatus().name();
        if (transfer.getBeneficiary() != null) {
            this.beneficiaryDetails = new BeneficiaryDetails(transfer.getBeneficiary());
        }
        this.creditAmount = transfer.getCreditAmount();
        if (transfer.getCreditCurrency() != null) {
            this.creditCurrency = transfer.getCreditCurrency().getCode();
        }
        this.creditValueDate = transfer.getCreditValueDate();
        if (transfer.getBeneficiary() != null) {
            this.creditTheirRef = transfer.getBeneficiary().getBeneficiaryReference();
        }

        this.debitAccountNumber = transfer.getSourceAccount().getAccountNumber();
        this.recipient = beneficiaryDetails != null ? beneficiaryDetails.getBeneficiaryFullname() : creditAccountNumber;

    }
}
