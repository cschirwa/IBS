package com.kt.ibs.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
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
@Table(name = "ibs_account_limit")
@Getter
@ToString
@Setter
@NoArgsConstructor
public class AccountLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Account account;

    private BigDecimal dailyOnlineTransations;
    private BigDecimal monthlyOnlineTransations;
    private BigDecimal ownAccountTransations;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

}
