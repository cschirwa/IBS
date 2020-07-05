package com.kt.ibs.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ibs_session")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sessionId = UUID.randomUUID().toString();
    private String token;
    
    @Enumerated(EnumType.STRING)
    private AuthorityName authority;

    @OneToOne
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date loginTime = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date logoutTime;

    private String devicePlatform;
    private Boolean mobile;

    @Embedded
    private Address loginLocation;

    public Session(String token, User user, Address loginLocation) {
        this.token = token;
        this.user = user;
        user.getAuthorities().stream().findFirst().ifPresent(auth -> this.authority = auth.getName());
        this.loginLocation = loginLocation;
    }

}
