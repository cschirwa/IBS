package com.kt.ibs.controllers.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAuditOutput implements Serializable {

    private String token;
    private String customerId;
    private String username;
    private Date eventDate;
    private String eventType;
}
