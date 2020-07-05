package com.kt.ibs.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "ibs_beneficiary")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid = UUID.randomUUID().toString();

    @Column(updatable = false)
    private String beneficiaryFullname;

    private String beneficiaryReference;

    private String senderReference;

    @Enumerated(EnumType.STRING)
    private PaymentCategory paymentCategory;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Account account;

    @ManyToOne
    private Customer customer;

    private boolean active;
    
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private String notificationDetails;
    
    @ManyToOne
    private Currency currency;
    
    @ManyToOne
    private Country country;

    @OneToOne(fetch = FetchType.EAGER)
    private CustomerAuthorization authorization;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

    public Beneficiary(final Customer customer,
            final String beneficiaryFullname,
            final PaymentCategory paymentCategory,
            final String beneficiaryReference,
            final String senderReference) {
        this.customer = customer;
        this.beneficiaryFullname = beneficiaryFullname;
        this.paymentCategory = paymentCategory;
        this.beneficiaryReference = beneficiaryReference;
        this.senderReference = senderReference;
        this.active = false;

    }

    public BankBranch getBranch() {
        return account.getBranch();
    }

}
