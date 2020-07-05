package com.kt.ibs.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.ibs.controllers.vo.Analytics;
import com.kt.ibs.entity.AuthorityName;
import com.kt.ibs.entity.Bank;
import com.kt.ibs.entity.Session;
import com.kt.ibs.entity.TransactionStatus;
import com.kt.ibs.entity.Transfer;
import com.kt.ibs.repository.CustomerAuditRepository;
import com.kt.ibs.repository.SessionRepository;
import com.kt.ibs.repository.TransferRepository;

@Service
public class AdminQueryService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private CustomerAuditRepository customerAuditRepository;

    public Analytics fetchAdminStats() {
        Analytics stats = new Analytics();
        List<Transfer> successToday = transferRepository.findByTransactionStatusAndProcessingDateGreaterThan(TransactionStatus.SUCCESS, DateUtil.START_OF_DAY);
        List<Transfer> failedToday = transferRepository.findByTransactionStatusAndProcessingDateGreaterThan(TransactionStatus.FAILED, DateUtil.START_OF_DAY);

        List<Session> sessions = sessionRepository.findByAuthorityAndLoginTimeGreaterThan(AuthorityName.ROLE_RETAIL_CUSTOMER, DateUtil.START_OF_DAY);

        Map<String, Integer> auditCounts = new HashMap<>();
        customerAuditRepository.findByEventDateGreaterThan(DateUtil.START_OF_DAY)
                .forEach(audit -> {
                    Integer total = auditCounts.get(audit.getEventType());
                    if (total == null) {
                        total = 0;
                        auditCounts.put(audit.getEventType(), ++total);
                    }
                });
        stats.setAuditCounts(auditCounts);

        BigDecimal totalSuccess = BigDecimal.ZERO;
        BigDecimal totalFailed = BigDecimal.ZERO;

        Map<String, Integer> totalByBank = new HashMap<>();
        Map<String, BigDecimal> amountByBank = new HashMap<>();
        successToday.forEach(success -> {
            Bank bank = success.getBeneficiary().getAccount().getBranch().getBank();
            Integer bankTotal = totalByBank.get(bank.getBankCode());
            if (bankTotal == null) {
                bankTotal = 0;
                totalByBank.put(bank.getBankCode(), ++bankTotal);
            }
            BigDecimal bankAmount = amountByBank.get(bank.getBankCode());
            if (bankAmount == null) {
                bankAmount = BigDecimal.ZERO;
                amountByBank.put(bank.getBankCode(), bankAmount.add(success.getCreditAmount()));
            }

            totalSuccess.add(success.getCreditAmount());
        });

        failedToday.forEach(failed -> {
            totalFailed.add(failed.getCreditAmount());
        });
        stats.setTotalFailedTransactionsToday(totalFailed);
        stats.setTotalTransactionsToday(totalSuccess);
        Long totalMobile = sessions.stream()
                .filter(session -> session.getMobile() != null)
                .filter(session -> session.getMobile())
                .count();
        stats.setTotalLoginsToday(sessions.size());
        stats.setTotalMobileLoginsToday(totalMobile);
        stats.setAmountByBank(amountByBank);
        stats.setTotalByBank(totalByBank);
        return stats;

    }

}
