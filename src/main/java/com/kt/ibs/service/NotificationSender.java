package com.kt.ibs.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.kt.ibs.configuration.ApplicationConfigurationProperties;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationSender {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private ApplicationConfigurationProperties applicationConfigurationProperties;

    public void sendEmail(final String from, final String to, final String subject, final String message) {
        log.info("Mail Send for to [{}], subject [{}], message [{}]", to, subject, message);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setBcc("evans@deltaceti.co.za");
        msg.setSubject(subject);
        msg.setTo(to);
        msg.setText(message);
        try {
            mailSender.send(msg);
        } catch (Exception ex) {
            log.error("Error sending notification", ex);
        }
    }

    @EventListener
    public void notify(final CustomerNotification notification) {
        if (applicationConfigurationProperties.isMailEnabled()) {
            sendEmail(notification.getFrom(), notification.getTo(), notification.getSubject(),
                    notification.getMessage());
        }
        if (applicationConfigurationProperties.isSmsEnabled()) {
            sendSms(notification.getMessage());
        }
    }

    private void sendSms(final String message) {
        try {
            // Construct data
            String data = "";
            /*
             * Note the suggested encoding for certain parameters, notably
             * the username, password and especially the message. ISO-8859-1
             * is essentially the character set that we use for message bodies,
             * with a few exceptions for e.g. Greek characters. For a full list,
             * see: http://developer.bulksms.com/eapi/submission/character-encoding/
             */
            data += "username=" + URLEncoder.encode(applicationConfigurationProperties.getSmsUser(), "ISO-8859-1");
            data += "&password=" + URLEncoder.encode(applicationConfigurationProperties.getSmsPassword(), "ISO-8859-1");
            data += "&message=" + URLEncoder.encode(message, "ISO-8859-1");
            data += "&want_report=1";
            data += "&msisdn=27835540735";

            // Send data
            // Please see the FAQ regarding HTTPS (port 443) and HTTP (port 80/5567)
            URL url = new URL(applicationConfigurationProperties.getSmsUrl());

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                log.info("Response from SMS API is {}", line);
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            log.error("Error send SMS", e);
        }
    }
}
