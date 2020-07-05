package com.kt.ibs.response;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "restResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class RestResponse {
    @XmlElement(name = "serviceResponseHeader")
    private ServiceResponseHeader serviceResponseHeader;
    private String message;
    private Object data;

    public ServiceResponseHeader getServiceResponseHeader() {
        return serviceResponseHeader;
    }

    public void setServiceResponseHeader(ServiceResponseHeader serviceResponseHeader) {
        this.serviceResponseHeader = serviceResponseHeader;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
