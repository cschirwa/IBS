package com.kt.ibs.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ibs_staff")

@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BankStaff extends User {


    private String branch;

    @ManyToOne
    private FinancialInstitution financialInstitution;

    public BankStaff(final String username, final String password, final FinancialInstitution financialInstitution) {
        super(username, password);
        this.getAuthorities().add(new Authority(AuthorityName.ROLE_ADMIN));
        this.financialInstitution = financialInstitution;
    }

    public BankStaff(final String username, final String password, final FinancialInstitution financialInstitution, final Authority... authority) {
        super(username, password, authority);
        this.financialInstitution = financialInstitution;
    }

}
