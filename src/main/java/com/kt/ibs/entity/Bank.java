package com.kt.ibs.entity;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "ibs_bank")
@Getter
@ToString
@NoArgsConstructor
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String bankCode;
    private String bankName;

    @OneToMany(mappedBy = "bank")
    private List<BankBranch> branches;

    @Embedded
    private Address address;

    @ManyToOne
    private Country country;

    public Bank(String bankCode, String bankName, Address address) {
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.address = address;
    }

    public Bank(String bankCode, String bankName, final Country country) {
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.country = country;
    }

}
