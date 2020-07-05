package com.kt.ibs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ibs_financial_institution")
@Getter
@ToString
@Setter
@NoArgsConstructor
public class FinancialInstitution {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String name;

    @ManyToOne
    private Currency defaultCurrency;

    @Enumerated(EnumType.STRING)
    private ChargeType chargeType;

    @ManyToOne
    private Country country;

}
