package com.kt.ibs.service;

import lombok.Getter;

@Getter
public enum SiteOwnerNotification {

    CONTACT("New contact us request",
            "User with name %s has submitted contact request with details <br />"
                    + "Name: %s <br />  "
                    + "Phone: %s <br />  "
                    + "Email: %s <br />"
                    + "Message: %s "),

    APPOINTMENT("Appointment request",
            "User with name %s has submitted reservation with details <br />"
                    + "Name: %s <br />  "
                    + "Phone: %s <br />  "
                    + "Email: %s <br />"),

    MAILING_LIST("Mailinglist request", "User with email %s has subscribed ");

    private final String subject;
    private final String message;

    SiteOwnerNotification(final String subject, final String message) {
        this.subject = subject;
        this.message = message;
    }

}
