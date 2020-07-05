package com.kt.ibs.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kt.ibs.controllers.vo.Transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ibs_transaction")
@Getter
@Setter
@NoArgsConstructor
public class AccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid = UUID.randomUUID().toString();

    @OneToOne
    @JoinColumn(name = "currency")
    private Currency currency;

    @ManyToOne
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private Boolean immediate;

    private String reference;
    private String targetReference;

    @ManyToOne
    private Account account;

    private BigDecimal amount;

    private BigDecimal closingBalance;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Temporal(TemporalType.DATE)
    private Date valueDate;

    @Temporal(TemporalType.DATE)
    private Date bookingDate;

    @OneToOne
    private Beneficiary beneficiary;
    @OneToOne
    private Account targetAccount;

    @OneToOne
    private Transfer transfer;

    private String description;

    public AccountTransaction(final Account account, final Transaction transaction) {
        this.account = account;
        this.amount = transaction.getTxnAmount();
        this.closingBalance = transaction.getClosingBalance();
        this.createdTime = transaction.getProcessedDate();
        this.valueDate = transaction.getValueDate();
        this.bookingDate = transaction.getBookingDate();
        this.description = transaction.getDescription();
        this.reference = transaction.getReference();
    }
    public String getBeneficiaryBank() {
        if (beneficiary != null) {
            return beneficiary.getBranch().getBank().getBankName();
        }
        return "";
    }

}
