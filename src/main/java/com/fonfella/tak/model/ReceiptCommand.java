package com.fonfella.tak.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ReceiptCommand {
//switch to check/get image

    String receiptCommand;

    //decommentare se dovesse servire un path differente per check scontrino
   // @NotEmpty
   // String pathReceipt;

    @NotEmpty
    String receiptValue;

}
