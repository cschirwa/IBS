package com.kt.ibs.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.kt.ibs.controllers.vo.AccountDetails;
import com.kt.ibs.controllers.vo.CreateAccountInputDetails;
import com.kt.ibs.controllers.vo.CreateAccountResponseDetails;
import com.kt.ibs.controllers.vo.CustomerDetails;
import com.kt.ibs.controllers.vo.Transaction;
import com.kt.ibs.entity.Account;
import com.kt.ibs.entity.AccountTransaction;
import com.kt.ibs.entity.AuditType;
import com.kt.ibs.entity.Currency;
import com.kt.ibs.entity.Customer;
import com.kt.ibs.entity.CustomerAudit;
import com.kt.ibs.repository.AccountRepository;
import com.kt.ibs.repository.AccountTransactionRepository;
import com.kt.ibs.repository.CurrencyRepository;
import com.kt.ibs.repository.CustomerRepository;
import com.kt.ibs.response.RestResponse;
import com.kt.ibs.validations.CreateAccountValidator;
import com.kt.ibs.validations.InputOutputResults;
import com.kt.ibs.validations.Notifications;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CoreBankingService coreBankingService;

    @Autowired
    private ApplicationEventPublisher auditPublisher;

    public RestResponse createAccount(final CreateAccountInputDetails accountDetails, final String username) {
        CreateAccountValidator validator = new CreateAccountValidator();
        RestResponse restServiceResponse = new RestResponse();
        InputOutputResults inputOutputResults = new InputOutputResults();
        try {

            Notifications[] notifications = validator.validateAccount(accountDetails);
            restServiceResponse.setServiceResponseHeader(inputOutputResults.determineResponse(notifications));

            if (notifications.length > 0) {
                return restServiceResponse;
            }
            Customer owner = customerRepository.findByUsername(username).get();
            CreateAccountResponseDetails createAccountResponseDetails = coreBankingService.createAccount(accountDetails, owner.getCustomerId());
            Account input = convertToEntity(createAccountResponseDetails);
            input.setCustomer(owner);
            Account savedInstance = accountRepository.save(input);
            restServiceResponse.setMessage("Account " + savedInstance.getAccountTitle1() + " created successfully");

            log.info("service task is complete");
        } catch (Exception e) {
            log.error("Error", e);
            restServiceResponse.setServiceResponseHeader(inputOutputResults.getExceptionResponse());
        }

        return restServiceResponse;
    }

    public RestResponse fetchAccounts(final String username, final String category) {
        Customer customer = customerRepository.findByUsername(username).get();
        if (customer.getAccounts().isEmpty()) {
            List<AccountDetails> coreAccounts = coreBankingService.fetchAccounts(customer.getCustomerId());
            coreAccounts.forEach(coreAccount -> updateLocalAccount(coreAccount, customer));
        }
        RestResponse restResponse = new RestResponse();
        InputOutputResults inputOutputResults = new InputOutputResults();
        restResponse.setMessage("");
        try {
            Customer updated = customerRepository.findByUsername(username).get();
            restResponse.setData(new CustomerDetails(updated, category));
            restResponse.setServiceResponseHeader(inputOutputResults.determineResponse(null));
            restResponse.setMessage("Success!");
        } catch (Exception e) {
            log.error("Error", e);
            restResponse.setServiceResponseHeader(inputOutputResults.getExceptionResponse());
        }
        return restResponse;
    }

    public void updateLocalAccount(final AccountDetails accountDetails, final Customer customer) {
        Account online = accountRepository.findByUsernameAndAccountNumber(customer.getUsername(), accountDetails.getAccountNumber().trim());
        if (online == null) {
            online = new Account();
            online.setAllowedOnline(true);
        }
        online.setUsername(customer.getUsername());
        online.setLastCoreSyncTime(new Date());
        online.setCustomer(customer);
        String coreCurrency = "USD";
        if (accountDetails.getCurrency() != null) {
            coreCurrency = accountDetails.getCurrency().trim();
        }
        Currency currency = currencyRepository.findByCode(coreCurrency);
        if ((currency == null) && !StringUtils.isEmpty(coreCurrency)) {
            log.info("Adding currency {}", coreCurrency);
            currency = currencyRepository.save(new Currency(coreCurrency, coreCurrency));
        }
        online.setCurrency(currency);
        online.setAccountCategory(accountDetails.getCategoryCode());
        online.setAccountType(accountDetails.getAccountType());
        online.setAccountNumber(accountDetails.getAccountNumber().trim());
        online.setAccountTitle1(accountDetails.getAccountName());
        online.setWorkingBalance(accountDetails.getWorkingBalance());
        online.setOnlineActualBalance(accountDetails.getOnlineActualBalance());
        online.setAccountLimit(accountDetails.getAccountLimit());
        if (accountRepository.findByUsernameAndAccountNumber(customer.getUsername(), accountDetails.getAccountNumber().trim()) == null) {
            accountRepository.save(online);
        }
    }

    public RestResponse fetchAccountTransactions(final String username, final String accountNumber) {
        Account account = accountRepository.findByUsernameAndAccountNumber(username, accountNumber);
        log.info("fetching with username {} and accountNumber {} returned {}", username, accountNumber, account.getWorkingBalance());
        List<Transaction> coreTransactions = coreBankingService.fetchTransactions(accountNumber);
        coreTransactions.forEach(coreTransaction -> {
            List<AccountTransaction> transactions = accountTransactionRepository.findByCustomerUsernameAndAccountAccountNumberAndReference(username, accountNumber, coreTransaction.getReference()
                    .trim());
            Optional<AccountTransaction> existingOnlineTransaction = transactions.stream().findFirst();
            AccountTransaction onlineTransaction = null;
            if (existingOnlineTransaction.isPresent()) {
                onlineTransaction = existingOnlineTransaction.get();
                onlineTransaction.setCustomer(account.getCustomer());
                onlineTransaction.setAccount(account);
                onlineTransaction.setClosingBalance(coreTransaction.getClosingBalance());

            } else {
                onlineTransaction = new AccountTransaction(account, coreTransaction);
                onlineTransaction.setCustomer(account.getCustomer());
                onlineTransaction.setAccount(account);
            }

            accountTransactionRepository.save(onlineTransaction);

        });

        RestResponse restResponse = new RestResponse();
        InputOutputResults inputOutputResults = new InputOutputResults();
        restResponse.setMessage("");
        final List<Transaction> transactionDetails = accountTransactionRepository.findByCustomerUsernameAndAccountAccountNumber(username, accountNumber).stream().map(Transaction::new).collect(
                Collectors.toList());
        if (transactionDetails != null) {
            Collections.sort(transactionDetails, (a, b) -> (a.getValueDate() == null) || (b.getValueDate() == null) ? 0 : a.getValueDate().compareTo(b.getValueDate()));
        }
        restResponse.setData(new AccountDetails(account, transactionDetails));
        restResponse.setServiceResponseHeader(inputOutputResults.determineResponse(null));
        restResponse.setMessage("Success!");
        auditPublisher.publishEvent(new CustomerAudit(null, null, accountNumber, AuditType.ACCOUNTS_LIST.name()));
        return restResponse;
    }

    private Account convertToEntity(final CreateAccountResponseDetails responseDetails) {
        Account account = new Account();
        account.setAccountNumber(responseDetails.getAccountNumber());
        account.setAccountCategory(responseDetails.getCategory());
        account.setAccountTitle1(responseDetails.getAccountTitle1());
        account.setAccountTitle2(responseDetails.getAccountTitle2());
        account.setCurrency(currencyRepository.findByCode(responseDetails.getCurrency()));
        account.setBroker(responseDetails.getBroker());
        account.setDateCreated(new Date());
        account.setShortTitle(responseDetails.getShortTitle());
        account.setPositionType(responseDetails.getPositionType());
        account.setLastCoreSyncTime(new Date());
        account.setWorkingBalance(new BigDecimal(responseDetails.getActualWorkingBalance()));
        account.setOnlineActualBalance(new BigDecimal(responseDetails.getOnlineBalance()));
        return account;
    }
}
