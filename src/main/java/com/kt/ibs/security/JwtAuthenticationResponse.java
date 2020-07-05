package com.kt.ibs.security;

import java.io.Serializable;
import java.util.List;

import com.kt.ibs.entity.AuthorityName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private String token;
    private String message;
    private List<AuthorityName> authorities;

    public String getToken() {
        return this.token;
    }
}
