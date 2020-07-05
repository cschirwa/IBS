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
@Table(name = "ibs_currency")
@Getter
@Setter
@NoArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(unique = true, updatable = false)
    private String code;

    private String description;
    private String symbol;

    public Currency(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    public Currency(final String code, final String description, final String symbol) {
        this(code, description);
        this.symbol = symbol;
    }

}
