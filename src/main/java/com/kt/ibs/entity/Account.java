package com.kt.ibs.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ibs_account")
@Getter
@ToString
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER, optional=false)
    private Currency currency;
    private String username;
    private boolean allowedOnline;
    private String accountCategory;
    private String accountType;
    private String accountTitle1;
    private String accountTitle2;
    private String shortTitle;
    private String mnemonic;
    private String positionType;
    private String broker;
    private String accountNumber;
    private BigDecimal accountLimit;
    private BigDecimal onlineActualBalance;
    private BigDecimal workingBalance;
    private BigDecimal onlineDailyLimit;
    private BigDecimal onlineMonthlyLimit;


    @Temporal(TemporalType.TIMESTAMP)
    private Date lastCoreSyncTime;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade=CascadeType.REMOVE)
    private List<AccountTransaction> transactions;
    @ManyToOne
    private BankBranch branch;

    @ManyToOne(fetch=FetchType.EAGER)
    private Customer customer;

    @OneToOne
    private Beneficiary beneficiary;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

    public static Account beneficiary(final String accountNumber, final Currency currency, final BankBranch branch) {
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBranch(branch);
        account.setCurrency(currency);
        return account;
    }

}
