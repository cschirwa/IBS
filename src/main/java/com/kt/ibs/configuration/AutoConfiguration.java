package com.kt.ibs.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableConfigurationProperties(ApplicationConfigurationProperties.class)
@Configuration
public class AutoConfiguration {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        return executor;
    }

    @Autowired
    private ApplicationConfigurationProperties mailConfigurationProperties;

    @Bean
    public MailSender mailSender() {
        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setHost(mailConfigurationProperties.getEmailHost());
        javaMailSenderImpl.setPort(mailConfigurationProperties.getEmailPort());
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", true);
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.smtp.auth", true);
        javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.put("mail.smtp.ssl.trust", "*");
        javaMailSenderImpl.setJavaMailProperties(javaMailProperties);
        javaMailSenderImpl.setUsername(mailConfigurationProperties.getMailUser());
        javaMailSenderImpl.setPassword(mailConfigurationProperties.getMailPassword());
        javaMailSenderImpl.setUsername("r035198x@gmail.com");
        javaMailSenderImpl.setPassword("two023tar695");
        return javaMailSenderImpl;
    }

}
