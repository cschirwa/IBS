package com.kt.ibs.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kt.ibs.controllers.vo.AccountDetails;
import com.kt.ibs.controllers.vo.Transaction;
import com.kt.ibs.entity.AccountTransaction;
import com.kt.ibs.repository.AccountRepository;
import com.kt.ibs.repository.AccountTransactionRepository;
import com.itextpdf.text.DocumentException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountTransactionService {

    private final static Logger LOGGER = Logger.getLogger(AccountTransactionService.class);
    
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;


    @Autowired
    private ApplicationEventPublisher auditPublisher;

    public ResponseEntity<InputStreamResource> generatePaymentReport(final String username, final String accountNumber, final String reference) {
        //List<AccountTransaction> transactions = accountTransactionRepository.findByCustomerUsernameAndAccountAccountNumberAndReference(username, accountNumber, reference);
       // AccountTransaction accountTransaction = transactions.get(0);
        final byte[] byteArray = StatementService.generateTransactionConfirmation(new AccountTransaction()).toByteArray();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        HttpHeaders responseHeaders = new HttpHeaders();
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        responseHeaders.setContentLength(byteArray.length);
        responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
        return new ResponseEntity<> (inputStreamResource,
                                   responseHeaders,
                                   HttpStatus.OK);

    }
    
    public ResponseEntity<InputStreamResource> generateStatementReport(final String username, final String accountNumber) throws DocumentException {
        List<AccountTransaction> transactions = accountTransactionRepository.findByCustomerUsernameAndAccountAccountNumber(username, accountNumber);
        List<Transaction> collected = transactions.stream()
        .map(Transaction::new)
        .collect(Collectors.toList());
        AccountDetails accountDetails = new AccountDetails(accountRepository.findByUsernameAndAccountNumber(username, accountNumber), collected);
        final byte[] byteArray = StatementService.createPDF(accountDetails).toByteArray();
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        HttpHeaders responseHeaders = new HttpHeaders();
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        responseHeaders.setContentLength(byteArray.length);
        responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
        return new ResponseEntity<> (inputStreamResource,
                                   responseHeaders,
                                   HttpStatus.OK);

    }

}
