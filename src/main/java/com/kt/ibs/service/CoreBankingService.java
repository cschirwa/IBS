package com.kt.ibs.service;

import java.util.List;

import com.kt.ibs.controllers.vo.AccountDetails;
import com.kt.ibs.controllers.vo.CreateAccountInputDetails;
import com.kt.ibs.controllers.vo.CreateAccountResponseDetails;
import com.kt.ibs.controllers.vo.CustomerDetails;
import com.kt.ibs.controllers.vo.Transaction;
import com.kt.ibs.controllers.vo.TransferInput;
import com.kt.ibs.controllers.vo.TransferResponseDetails;
import com.kt.ibs.entity.Account;
import com.kt.ibs.entity.Beneficiary;

public interface CoreBankingService {

    public CreateAccountResponseDetails createAccount(final CreateAccountInputDetails createAccountDetails, final String customerId);

    public List<AccountDetails> fetchAccounts(final String customerId);

    public List<Transaction> fetchTransactions(final String accountNumber);

    public TransferResponseDetails beneficiaryTransfer(final TransferInput transferInput, final Beneficiary beneficiary, final Account sourceAccount);

    public TransferResponseDetails onceOffTransfer(final TransferInput transferInput);

    public TransferResponseDetails internationalTransfer(final TransferInput transferInput);

    public TransferResponseDetails intraTransfer(final TransferInput transferInput);

    public CustomerDetails fetchCustomerDetails(String string);

    void fetchBranches();

}
