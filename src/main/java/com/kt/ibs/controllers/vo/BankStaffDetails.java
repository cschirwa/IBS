package com.kt.ibs.controllers.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankStaffDetails {

    private String username;
    private String uuid;
    private String name; 
    private String bank;
    private Date lastLoginDate;

}
