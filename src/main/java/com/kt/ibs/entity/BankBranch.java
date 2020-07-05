package com.kt.ibs.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//ibs_cus, cbs_cus,cbs_bank, ibs_bank 
@Table(name = "ibs_bank_branch")
@Getter
@NoArgsConstructor
public class BankBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @ManyToOne(fetch=FetchType.EAGER)
    private Bank bank;
    
    private String branchCode;
    private String branchName;
    
    @Embedded
    private Address address;
    public BankBranch(final Bank bank, final String branchCode, final String branchName) {
        super();
        this.bank = bank;
        this.branchCode = branchCode;
        this.branchName = branchName;
    }
    
    
}
