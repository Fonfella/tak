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

    //esempio
//    http://localhost:8080/robot
//    {
//        "robotCommand":"MBSTR WR PSH D7 @F" esempio di azione psh push d7 casella come battaglianavale @F frontale @R schermo posteriore
//    }


    @PostMapping ("/fingerBot")
    public JSONObject commandR(@Valid @RequestBody FingerBotCommand fingerBotCommand) throws JsonProcessingException, InterruptedException {
        log.info(fingerBotCommand.toString());
        return fingerBotService.sendFingerBot(fingerBotCommand);

    }
}
