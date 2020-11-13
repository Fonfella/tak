package com.vinz.tak.controller;

import com.vinz.tak.model.ReceiptCommand;
import com.vinz.tak.service.ReceiptService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CheckReceipt extends AbstractController {

    @Autowired
    private ReceiptService receiptService;


    @PostMapping("/receipt")
    public JSONObject commandR(@Valid @RequestBody ReceiptCommand receiptCommand)
    {
        log.info(receiptCommand.toString());
        return receiptService.sendReceiptCommand(receiptCommand);
    }
}
