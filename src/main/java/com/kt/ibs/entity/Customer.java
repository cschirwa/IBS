package com.kt.ibs.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ibs_customer")

@Getter
@Setter
@NoArgsConstructor
public class Customer extends User {

    @Id
    @SequenceGenerator(name = "CUST_PK_SEQ", sequenceName = "CUST_PK_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUST_PK_SEQ")
    private Long id;

    private String customerId;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private String notificationDetails; 
    private String mnemonic;
    private String customerName;
    private String accountOffice;
    private String nationality;
    private String residence;
    private String sector;
    private String industry;
    private String branch;

    private String additionalPhoneNumber1;

    private String additionalPhoneNumber2;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<Account> accounts;

    @OneToOne(fetch = FetchType.EAGER)
    private CustomerAuthorization authorization;

    public Customer(final String username, final String password) {
        super(username, password, AuthorityName.ROLE_RETAIL_CUSTOMER);
    }

    public Customer(final String username, final String password, final AuthorityName authority) {
        super(username, password, authority);
    }

    public Customer(final String username, final String password, final Authority... authority) {
        super(username, password, authority);
    }

}
