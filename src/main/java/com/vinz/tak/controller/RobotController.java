package com.vinz.tak.controller;

import com.vinz.tak.model.RobotCommand;
import com.vinz.tak.service.RobotService;
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
    public RobotCommand commandR(@Valid @RequestBody RobotCommand robotCommand) {

        return robotService.sendRobotCommand(robotCommand);
    }
}
