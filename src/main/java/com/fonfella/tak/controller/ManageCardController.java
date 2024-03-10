package com.fonfella.tak.controller;

import com.fonfella.tak.model.ManageCardCommand;
import com.fonfella.tak.service.ManageCardBotService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class ManageCardController extends AbstractController {

    @Autowired
    private ManageCardBotService manageCardBotService;

    //esempio richiede installazione del software sul device e setup corretto
//    http://localhost:8080/fingerBot
//    {
//        "deviceName": "Fonfella S10",
//        "platformVersion": "12.0.0",
//        "udid": "RF8M53XN05H"
//    }


    @PostMapping ("/manageCard")
    public JSONObject commandR(@Valid @RequestBody ManageCardCommand manageCardCommand) throws IOException, InterruptedException {
        log.info(manageCardCommand.toString());
        return manageCardBotService.sendManageCard(manageCardCommand);

    }
}
