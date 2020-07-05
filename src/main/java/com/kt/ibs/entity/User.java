package com.kt.ibs.entity;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class User {

    @Id
    @SequenceGenerator(name = "USER_PK_SEQ", sequenceName = "USER_PK_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_PK_SEQ")
    private Long id;
    
    private String uuid = UUID.randomUUID().toString();

    @Column(unique = true, updatable = false)
    private String username;

    @JsonIgnore
    private String password;

    private String fullname;

    private String phoneNumber;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    private String mobileNumber;

    private String branch;

    @Column(unique = true)
    private String email;

    private String alternativeEmail;

    private Boolean enabled;

    private Boolean locked;

    private int failedLogins;
    private int oneTimePin;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordResetDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date profileStartDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lockedDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//    @JoinTable(name = "USER_AUTHORITY", joinColumns = {
//            @JoinColumn(name = "USER_ID", referencedColumnName = "ID")}, inverseJoinColumns = {
//                    @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    private Set<Authority> authorities;

    public User(final String username, final String password) {
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.enabled = true;
        this.lastPasswordResetDate = new Date();
        this.email = username;
        this.authorities = new HashSet<>();
    }
    public void updatePassword(final String newPassword) {
        this.password = BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }

    public User(final String username, final String password, final AuthorityName authority) {
        this(username, password);
        authorities.add(new Authority(authority));
    }

    public User(final String username, final String password, final Authority... authority) {
        this(username, password);
        if (authorities == null) {
            authorities = new HashSet<>();
        }
        authorities.addAll(Arrays.asList(authority));
    }

}

