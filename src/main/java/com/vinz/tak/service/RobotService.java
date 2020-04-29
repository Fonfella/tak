package com.vinz.tak.service;

import com.vinz.tak.model.ExecResult;
import com.vinz.tak.model.ProcessOptions;
import com.vinz.tak.model.RobotCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotService extends AbstractService {

    @Autowired
    private ProcessExecutor processExecutor;

    public RobotCommand sendRobotCommand(RobotCommand robotCommand) {
        ProcessOptions options = new ProcessOptions();
        String cazzo = robotCommand.getStoCazzo();
        ExecResult dir = processExecutor.shellExec(options, cazzo);
        log.info(String.valueOf(dir));
        log.info("command execute" + robotCommand);
        return robotCommand;
    }
}
