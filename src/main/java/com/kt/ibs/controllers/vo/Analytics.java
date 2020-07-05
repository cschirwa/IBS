package com.kt.ibs.controllers.vo;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Analytics {

    private long totalMobileLoginsToday;
    private int totalLoginsToday;
    private int totalFailedLoginsToday;

    private int totalRegistrationsToday;
    private int totalFailedRegistrationsToday;

    private BigDecimal totalTransactionsToday;
    private BigDecimal totalFailedTransactionsToday;
    
    Map<String, Integer> totalByBank;
    Map<String, Integer> auditCounts;
    Map<String, BigDecimal> amountByBank;


}
