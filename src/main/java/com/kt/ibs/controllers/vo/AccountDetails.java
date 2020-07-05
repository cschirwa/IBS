package com.kt.ibs.controllers.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.kt.ibs.entity.Account;
import com.kt.ibs.service.Util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AccountDetails implements Comparable<AccountDetails> {

    private String accountType;
    private String currency;
    private BigDecimal onlineActualBalance;
    private BigDecimal workingBalance;

    private BigDecimal clearedBalance;
    private BigDecimal lockedBalance;
    
    private BigDecimal accountLimit;
    
    private String accountNumber;
    private String accountName;
    private String categoryCode;
    private List<Transaction> transactions;
    private String workingBalanceFormatted;
    private boolean allowedOnline;
    
    private BigDecimal onlineDailyLimit;
    private BigDecimal onlineMonthlyLimit;

    public AccountDetails(final Account account, final List<Transaction> transactions) {
        this.categoryCode = account.getAccountCategory();
        this.accountType = account.getAccountType();
        if (account.getCurrency() != null) {
            this.currency = account.getCurrency().getCode();
        }
        this.onlineActualBalance = account.getOnlineActualBalance();
        this.workingBalance = account.getWorkingBalance();
        this.accountNumber = account.getAccountNumber();
        this.accountName = account.getAccountTitle1();
        this.transactions = transactions;
        this.allowedOnline = account.isAllowedOnline();
        this.onlineDailyLimit = account.getOnlineDailyLimit();
        this.onlineDailyLimit = account.getOnlineMonthlyLimit();
        this.workingBalanceFormatted = Util.formatMoney(currency, account.getWorkingBalance());

    }

    public String getId() {
        return this.accountNumber;
    }

    public String getText() {
        return this.accountType + " - " + this.accountNumber + " | "+this.getCurrency()+ " " + Util.formatMoney(this.currency, workingBalance) ;
    }

    @Override
    public int compareTo(final AccountDetails o) {
        return Objects.compare(accountNumber, o.getAccountNumber(), (a,b) -> a.compareTo(b));
    }

}
