package com.kt.ibs.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "ibs_transfer")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid = UUID.randomUUID().toString();

    private TransactionType transactionType;

    private String paymentMethod;

    private String currencymktdr;

    @OneToOne
    private Currency debitCurrency;

    private BigDecimal debitAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date debitValueDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date indebitValueDate;

    private String currencymktcr;

    @OneToOne
    private Currency creditCurrency;

    private BigDecimal creditAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creditValueDate;

    private String treasuryRate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date processingDate;
    
    private String coreReference;

    @ManyToOne
    private Account sourceAccount;

    @ManyToOne
    private Account targetAccount;

    @ManyToOne
    private Beneficiary beneficiary;

    @ManyToOne
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    @OneToOne(fetch = FetchType.EAGER)
    private CustomerAuthorization authorization;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

    public Transfer(final TransactionType transactionType, final String paymentMethod, final String currencymktdr,
                    final Currency debitCurrency, final BigDecimal debitAmount, final Date debitValueDate,
                    final Date indebitValueDate, final String currencymktcr, final Currency creditCurrency,
                    final BigDecimal creditAmount, final Date creditValueDate, final String treasuryRate,
                    final Date processingDate, final Account sourceAccount, final Account targetAccount,
                    final Beneficiary beneficiary, final Customer customer, final TransferType transferType) {
        this.transactionType = transactionType;
        this.paymentMethod = paymentMethod;
        this.currencymktdr = currencymktdr;
        this.debitCurrency = debitCurrency;
        this.debitAmount = debitAmount;
        this.debitValueDate = debitValueDate;
        this.indebitValueDate = indebitValueDate;
        this.currencymktcr = currencymktcr;
        this.creditCurrency = creditCurrency;
        this.creditAmount = creditAmount;
        this.creditValueDate = creditValueDate;
        this.treasuryRate = treasuryRate;
        this.processingDate = processingDate;
        this.sourceAccount = sourceAccount;
        this.beneficiary = beneficiary;
        this.customer = customer;
        this.transferType = transferType;
        this.targetAccount = targetAccount;
    }

}
