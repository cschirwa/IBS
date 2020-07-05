package com.kt.ibs.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ibs_cust_auth")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CustomerAuthorization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAuthorized;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateInitiated;

    private String otp;
    private int authAttempts;

    @Enumerated(EnumType.STRING)
    private OtpType otpType;

    public CustomerAuthorization(Customer customer, Date expiryDate, String otp, OtpType otpType) {
        super();
        this.customer = customer;
        this.expiryDate = expiryDate;
        this.otp = otp;
        this.otpType = otpType;
        this.dateInitiated = new Date();
    }

}
