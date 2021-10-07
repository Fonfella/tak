package com.vinz.tak.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vinz.tak.model.RobotCommand;
import com.vinz.tak.service.RobotService;
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


    @PostMapping("/robot")
    public JSONObject commandR(@Valid @RequestBody RobotCommand robotCommand) throws JsonProcessingException, InterruptedException {
        log.info(robotCommand.toString());
        return robotService.sendRobotCommand(robotCommand);
    }
}
