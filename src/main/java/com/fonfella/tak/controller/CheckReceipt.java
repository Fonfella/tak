package com.fonfella.tak.controller;

import com.fonfella.tak.model.ReceiptCommand;
import com.fonfella.tak.service.ReceiptService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class CheckReceipt extends AbstractController {

    @Autowired
    private ReceiptService receiptService;


    //esempio
//    POST http://localhost:8080/receipt
//    {
//        "receiptValue":"prova1-prova2-0,00 €"
//    } i valori da controllare devono essere separati da '-'

    @PostMapping("/receipt")
    public JSONObject commandR(@Valid @RequestBody ReceiptCommand receiptCommand) throws IOException {
        log.info(receiptCommand.toString());
        return receiptService.sendReceiptCommand(receiptCommand);
    }
}
