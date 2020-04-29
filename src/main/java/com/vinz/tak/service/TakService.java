package com.vinz.tak.service;

import com.vinz.pololu.api.ServoController;
import com.vinz.tak.model.Command;
import com.vinz.tak.model.ServoCommand;
import com.vinz.tak.pool.ServoControllerDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Component
public class TakService extends AbstractService
{
    @Value("${tak.security.token:pmicro}")
    public String SECURITY_TOKEN;

    @Autowired
    private ServoControllerDiscovery discovery;

    @Autowired
    private ServoCommandFactory servoCommandFactory;

    public void tak(Command command) throws Exception
    {
        validateServo(command);

        List<ServoCommand> servoCommands = servoCommandFactory.buildCommand(command);

        runServoController(servoCommands);
    }

    private void waitFor(ServoCommand servoCommand)
    {
        try
        {
            Thread.sleep(servoCommand.getDelay());
        }
        catch (InterruptedException ignored)
        {
        }
    }

    private void validateServo(Command command)
    {
        String servo = command.getServo().toLowerCase();

        if (servo.equals(servoCommandFactory.SERVO_ID_A))
        {
            return;
        }

        if (servo.equals(servoCommandFactory.SERVO_ID_B))
        {
            return;
        }

        if (servo.equals(servoCommandFactory.SERVO_ID_C))
        {
            return;
        }

        if (servo.equals(servoCommandFactory.SERVO_ID_D))
        {
            return;
        }

        exceptions.BadRequest("Invalid servo id [field: 'servo']");
    }

    public void debug(Map<String, Object> values) throws Exception
    {
        ServoCommand.ServoCommandBuilder builder = ServoCommand.builder();

        builder.servo(String.valueOf(values.get("servo")));
        builder.position(((Double) values.get("position")).floatValue());
        builder.speed(((Integer) values.get("speed")).shortValue());
        builder.acceleration(((Integer) values.get("acceleration")).shortValue());

        runServoController(Collections.singletonList(builder.build()));
    }

    private void runServoController(List<ServoCommand> servoCommands) throws Exception
    {
        ServoController servoController = null;

        try
        {
            servoController = discovery.getServoController();

            for (ServoCommand servoCommand : servoCommands)
            {
                servoController.raw(
                    servoCommand.getServo(),
                    servoCommand.getPosition(),
                    servoCommand.getSpeed(),
                    servoCommand.getAcceleration());

                waitFor(servoCommand);
            }
        }
        finally
        {
            if (servoController != null)
            {
                discovery.returnServoController(servoController);
            }
        }
    }
}
