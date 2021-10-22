package com.fonfella.tak.service;

import com.fonfella.tak.model.ExecResult;
import com.fonfella.tak.model.ProcessOptions;
import com.fonfella.tak.model.RobotCommand;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotService extends AbstractService {

    @Autowired
    private ProcessExecutor processExecutor;

    boolean stop = false;
    String timeout1 = "TIMEOUT 1";
    String read = "MBSTR RD";
    String output = "TYPE Output.txt";


    public JSONObject sendRobotCommand(RobotCommand robotCommand) throws InterruptedException {
        ProcessOptions options = new ProcessOptions();
        JSONObject obj = new JSONObject();
        ExecResult dir = processExecutor.shellExec(options, robotCommand.getRobotCommand());
        String result1 = (String.valueOf(dir));
 //       log.info(result1);
        log.info("command execute" + robotCommand);
        while (stop == false) {
          Thread.sleep(5000);
          processExecutor.shellExec(options, timeout1);
          processExecutor.shellExec(options, read);
          ExecResult dir1 = processExecutor.shellExec(options, output);
          log.info(String.valueOf(dir1.getLines()));
          String[] stArr = String.valueOf(dir1.getLines()).split("line=");
              if (stArr[1].contains("READY")) {
                  stop = true;
                  result1 = "READY";
                  obj.put("info", "Controller is ready!");
              } if (stArr[1].contains("Server is not responding.")) {
                  stop = true;
                  result1="Server is not responding!";
                  obj.put("info", "ERROR, Please check the connection with the server or that the server is turned on");
             }
        }
        stop = false;
        String result = (result1);
        obj.put("easyModBusStatus", result1);
        return obj;
    }
}
