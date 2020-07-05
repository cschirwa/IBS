package com.kt.ibs.controllers.vo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerConfirmationRequest {

    private String oneTimePin;
    private String customerUuid;
}
