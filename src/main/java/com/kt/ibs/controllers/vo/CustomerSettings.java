package com.kt.ibs.controllers.vo;

import java.util.Date;

import com.kt.ibs.entity.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSettings {

    private NotificationType notificationType;
    private String notificationDetails;
    private String oldPassword;
    private String newPassword; 
    private Date confirmOldPassword;

}
