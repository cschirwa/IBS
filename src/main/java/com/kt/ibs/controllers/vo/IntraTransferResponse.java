package com.kt.ibs.controllers.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IntraTransferResponse {

    private final boolean success;
    private TransferResponseDetails transferResponseDetails;
}
