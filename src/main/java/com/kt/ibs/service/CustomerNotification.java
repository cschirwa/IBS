package com.kt.ibs.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerNotification {

    
    private final String from;
    private final String to;
    private final String subject;
    private final String message;
}
