package com.kt.ibs.controllers.vo;

import com.kt.ibs.entity.Account;
import com.kt.ibs.entity.Customer;
import com.kt.ibs.entity.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Setter
@ToString
public class CustomerDetails {

    private String customerId;
    private String userName;
    private String firstName;
    private String surname;
    private Date lastLoginDate;
    private BigDecimal netPosition;
    private NotificationType notificationType;
    private Set<AccountDetails> accounts = new HashSet<>();

    private String mnemonic;
    private String customerName;
    private String accountOffice;
    private String nationality;
    private String residence;
    private String sector;
    private String industry;
    private String dateOfBirth;
    private String phoneNumber;
    private String mobileNumber;
    private String email;
    private String branch;

    public CustomerDetails(final Customer customer, final String category) {
        Integer categoryStart = Integer.valueOf(category.split("-")[0].trim());
        Integer categoryEnd = Integer.valueOf(category.split("-")[0].trim());
        this.customerId = customer.getCustomerId();
        this.firstName = customer.getFullname();
        this.userName = customer.getUsername();
        this.lastLoginDate = customer.getLastLoginTime();
        this.notificationType = customer.getNotificationType();
        netPosition = BigDecimal.ZERO;
        this.accounts = customer.getAccounts().stream()
                .filter(Account::isAllowedOnline)
                .filter(account -> account.getAccountCategory().startsWith(category) )
                .map(account -> {
                    AccountDetails accountDetails = new AccountDetails();
                    accountDetails.setCategoryCode(account.getAccountCategory());
                    accountDetails.setAccountName(account.getAccountTitle1());
                    accountDetails.setAccountNumber(account.getAccountNumber());
                    accountDetails.setAccountType(account.getAccountType());
                    accountDetails.setCurrency(account.getCurrency().getCode());
                    accountDetails.setOnlineActualBalance(account.getOnlineActualBalance());
                    accountDetails.setWorkingBalance(account.getWorkingBalance());
                    if (account.getOnlineActualBalance() != null) {
                        netPosition = netPosition.add(account.getOnlineActualBalance());
                    }
                    String format = "";

                    if (account.getCurrency().getCode().equals("ZAR")) {
                        format = NumberFormat.getCurrencyInstance(new Locale("en", "ZA"))
                                .format(account.getWorkingBalance());
                    } else {
                        format = NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                                .format(account.getWorkingBalance());
                    }
                    accountDetails.setWorkingBalanceFormatted(format);

                    return accountDetails;
                }).collect(Collectors.toSet());

    }

}
