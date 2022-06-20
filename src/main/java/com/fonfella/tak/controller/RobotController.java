package com.fonfella.tak.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fonfella.tak.model.RobotCommand;
import com.fonfella.tak.service.RobotService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RobotController extends AbstractController {

    @Autowired
    private RobotService robotService;

    //esempio
//    http://localhost:8080/robot
//    {
//        "robotCommand":"MBSTR WR PSH D7 @F" esempio di azione psh push d7 casella come battaglianavale @F frontale @R schermo posteriore
//    }


    @PostMapping("/robot")
    public JSONObject commandR(@Valid @RequestBody RobotCommand robotCommand) throws JsonProcessingException, InterruptedException {
        log.info(robotCommand.toString());
        return robotService.sendRobotCommand(robotCommand);
    }
}
