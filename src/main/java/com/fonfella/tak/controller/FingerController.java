package com.fonfella.tak.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fonfella.tak.model.FingerBotCommand;
import com.fonfella.tak.service.FingerBotService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class FingerController extends AbstractController {

    @Autowired
    private FingerBotService fingerBotService;

    //esempio richiede installazione del software sul device e setup corretto
//    http://localhost:8080/fingerBot
//    {
//        "deviceName": "Fonfella S10",
//        "platformVersion": "12.0.0",
//        "udid": "RF8M53XN05H"
//    }


    @PostMapping ("/fingerBot")
    public JSONObject commandR(@Valid @RequestBody FingerBotCommand fingerBotCommand) throws JsonProcessingException, InterruptedException {
        log.info(fingerBotCommand.toString());
        return fingerBotService.sendFingerBot(fingerBotCommand);

    }
}
