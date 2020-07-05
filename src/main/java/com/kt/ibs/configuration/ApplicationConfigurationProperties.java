package com.kt.ibs.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties
public class ApplicationConfigurationProperties {

    private int maxLogInAttempts = 3;
    
    private boolean mailEnabled;
    private String emailHost;
    private int emailPort;
    private String mailUser;
    private String mailPassword;

    private boolean smsEnabled;
    private String smsUrl;
    private String smsUser;
    private String smsPassword;
    
    private String branchesFolder; 
    
}
