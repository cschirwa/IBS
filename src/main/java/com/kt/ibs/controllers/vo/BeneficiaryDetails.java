package com.kt.ibs.controllers.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.kt.ibs.entity.Beneficiary;
import com.kt.ibs.entity.NotificationType;
import com.kt.ibs.entity.PaymentCategory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BeneficiaryDetails implements Serializable{

    private String beneficiaryFullname;
    private String beneficiaryReference;
    private String senderReference;
    private PaymentCategory paymentCategory;
    private String accountType;
    private String accountNumber;
    private String accountName;
    private String branchCode;
    private String bankCode;
    private String currency;
    private Date dateAdded;
    private BigDecimal lastPayment;
    private Date lastPaymentDate;
    private String country;
    private NotificationType notificationType;
    private String notificationDetails;
    
    private boolean active;
    private String uuid;
    private List<TransferResponseDetails> payments;
    public BeneficiaryDetails(final Beneficiary entity) {
        this.beneficiaryFullname = entity.getBeneficiaryFullname();
        this.beneficiaryReference = entity.getBeneficiaryReference();
        this.branchCode = entity.getBranch().getBranchCode();
        this.currency = entity.getAccount().getCurrency().getCode();
        this.accountNumber = entity.getAccount().getAccountNumber();
        this.accountName = entity.getAccount().getAccountTitle1();
        this.accountType = entity.getAccount().getAccountCategory();
        this.active = entity.isActive();
        this.senderReference = entity.getSenderReference();
        this.paymentCategory = entity.getPaymentCategory();
        this.uuid = entity.getUuid();
        this.bankCode = entity.getAccount().getBranch().getBank().getBankCode();
        this.dateAdded = entity.getDateCreated();
        this.notificationDetails = entity.getNotificationDetails();
        this.notificationType = entity.getNotificationType();
        this.country =entity.getCountry().getCode();

    }

}
