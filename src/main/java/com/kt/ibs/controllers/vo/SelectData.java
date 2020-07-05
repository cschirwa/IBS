package com.kt.ibs.controllers.vo;

import com.kt.ibs.entity.Bank;
import com.kt.ibs.entity.BankBranch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SelectData {

    private String id;
    private String text;
    private boolean selected;
    
    public SelectData(Bank bank) {
        this.id = bank.getBankCode();
        this.text = bank.getBankName();
    }
    
    
    public SelectData(BankBranch bank) {
        this.id = bank.getBranchCode();
        this.text = bank.getBranchName();
    }

}
