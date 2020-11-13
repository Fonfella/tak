package com.vinz.tak.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ReceiptCommand {

    @NotEmpty
    String receiptCommand;

    @NotEmpty
    String pathReceipt;

    @NotEmpty
    String receiptValue;

}
