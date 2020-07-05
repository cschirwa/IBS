package com.kt.ibs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ibs_country")
@Getter
@Setter
@NoArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(unique = true, updatable = false)
    private String code;

    private String description;
    private String locale;

    public Country(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    public Country(final String code, final String description, final String locale) {
        this(code, description);
        this.locale = locale;
    }

}
