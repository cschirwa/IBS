package com.kt.ibs.entity;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@NoArgsConstructor
@Embeddable
public class Address {

    private String line1;
    private String line2;
    public Address(String line1, String line2) {
        super();
        this.line1 = line1;
        this.line2 = line2;
    }
    
    
    
}
