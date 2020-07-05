package com.kt.ibs.response;


import com.kt.ibs.validations.Notifications;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;

@XmlRootElement(name = "serviceResponseHeader")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceResponseHeader {

    private int resultCode;
    private Notifications[] notifications;
    private String timestamp;
    private String resultDescription;
    private String uniqueTransactionId;

    public ServiceResponseHeader(int resultCode, Notifications[] notifications, String timestamp,
                                 String resultDescription, String uniqueTransactionId) {
        super();
        this.resultCode = resultCode;
        this.notifications = notifications;
        this.timestamp = timestamp;
        this.resultDescription = resultDescription;
        this.uniqueTransactionId = uniqueTransactionId;
    }

    public ServiceResponseHeader() {

    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Notifications[] getNotifications() {
        return notifications;
    }

    public void setNotifications(Notifications[] notifications) {
        this.notifications = notifications;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public String getUniqueTransactionId() {
        return uniqueTransactionId;
    }

    public void setUniqueTransactionId(String uniqueTransactionId) {
        this.uniqueTransactionId = uniqueTransactionId;
    }

    @Override
    public String toString() {
        return "ServiceResponse [ getResultCode()=" + getResultCode()
                + ", getNotifications()=" + Arrays.toString(getNotifications()) + ", getTimestamp()=" + getTimestamp()
                + ", getResultDescription()=" + getResultDescription() + ", getUniqueTransactionId()="
                + getUniqueTransactionId() + "]";
    }

}
