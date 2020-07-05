package com.kt.ibs.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ibs_customer_audit")
@Getter
@ToString
@Setter
@NoArgsConstructor
public class CustomerAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    private String customerId;
    private String username;
    private String accountNumber;
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate = new Date();
    private String eventType;

    public CustomerAudit(final String customerId, final String username, final String accountNumber, final String eventType) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.eventType = eventType;
        this.username = username;

    }

    public CustomerAudit(final String eventType) {
        this.eventType = eventType;
    } 

//    public CustomerAudit forRequest(final RequestData requestData) {
//        this.token = requestData.getToken();
//        this.customerId = requestData.getUserId();
//        return this;
//    }

}
