package com.kt.ibs.controllers.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRegInput {
    private String username;
    private String fullname;
    private String customerNumber;
    private String password;
    private String repeatPassword;
    private String emailAddress;
    private String alternativeEmailAddress;
    private String customerUuid;
    private String dateOfBirth;
    private String phoneNumber;
    private String notificationChannel;
}
