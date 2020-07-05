package com.kt.ibs.service;

import com.kt.ibs.controllers.vo.Transaction;
import com.kt.ibs.entity.AccountTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.ibs.entity.Customer;
import com.kt.ibs.entity.NotificationType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    public NotificationSender sender;

    public void sendOTPNotification(final Customer customer, final String message) {
        if (customer.getNotificationType() == NotificationType.EMAIL) {
            log.info("Triggering email for message {}", message);
            sender.notify(new CustomerNotification("ibs@deltaceti.co.za", customer.getEmail(), "IBS OTP", message));
        }
        if (customer.getNotificationType() == NotificationType.SMS) {
            sender.notify(new CustomerNotification("ibs@deltaceti.co.za", customer.getPhoneNumber(), "IBS OTP", message));
        }
    }
    public void sendPaymentNotification(final AccountTransaction accountTransaction, final String message){
        Customer customer= accountTransaction.getCustomer();
        if (customer.getNotificationType() == NotificationType.EMAIL) {
            log.info("Triggering email for message {}", message);
            sender.notify(new CustomerNotification("ibs@deltaceti.co.za", customer.getEmail(), "Payment Notification", message));
        }
        if (customer.getNotificationType() == NotificationType.SMS) {
            sender.notify(new CustomerNotification("ibs@deltaceti.co.za", customer.getPhoneNumber(), "Payment Notification", message));
        }
    }

}
