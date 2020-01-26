package com.vinz.tak.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vinz.tak.model.Command;
import com.vinz.tak.model.ServoCommand;


@Component
public class ServoCommandFactory
{
    @Value("${tak.servo.id.a:a}")
    public String SERVO_ID_A;

    @Value("${tak.servo.id.b:b}")
    public String SERVO_ID_B;

    @Value("${tak.servo.a.start:0.0}")
    private float SERVO_A_START;

    @Value("${tak.servo.b.start:1.0}")
    private float SERVO_B_START;

    @Value("${tak.servo.a.end:1.0}")
    private float SERVO_A_END;

    @Value("${tak.servo.b.end:0.0}")
    private float SERVO_B_END;

    @Value("${tak.servo.down.time:1000}")
    private short SERVO_DOWN_TIME;

    @Value("${tak.servo.down.speed:400}")
    private short SERVO_DOWN_SPEED;

    @Value("${tak.servo.down.acceleration:200}")
    private short SERVO_DOWN_ACCELERATION;

    @Value("${tak.servo.up.time:1000}")
    private short SERVO_UP_TIME;

    @Value("${tak.servo.up.speed:100}")
    private short SERVO_UP_SPEED;

    @Value("${tak.servo.up.acceleration:200}")
    private short SERVO_UP_ACCELERATION;

    private Map<String, Float> startPositions = new HashMap<>();

    private Map<String, Float> endPositions = new HashMap<>();

    @PostConstruct
    public void init()
    {
        startPositions.put(SERVO_ID_A, SERVO_A_START);
        startPositions.put(SERVO_ID_B, SERVO_B_START);

        endPositions.put(SERVO_ID_A, SERVO_A_END);
        endPositions.put(SERVO_ID_B, SERVO_B_END);
    }

    public List<ServoCommand> buildCommand(Command command)
    {
        List<ServoCommand> commands = new ArrayList<>();

        ServoCommand.ServoCommandBuilder builder;
        ServoCommand servoCommand;

        builder = ServoCommand.builder();
        builder.servo(command.getServo());
        builder.position(getStartPosition(command.getServo()));
        builder.speed(SERVO_DOWN_SPEED);
        builder.acceleration(SERVO_DOWN_ACCELERATION);
        builder.delay(SERVO_DOWN_TIME);

        servoCommand = builder.build();
        commands.add(servoCommand);

        if (command.getWait() > 0)
        {
            servoCommand.setDelay(command.getWait());

            commands.add(servoCommand);
        }

        builder = ServoCommand.builder();
        builder.servo(command.getServo());
        builder.position(getEndPosition(command.getServo()));
        builder.speed(SERVO_UP_SPEED);
        builder.acceleration(SERVO_UP_ACCELERATION);
        builder.delay(SERVO_UP_TIME);

        servoCommand = builder.build();
        commands.add(servoCommand);

        return commands;
    }

    private float getStartPosition(String servo)
    {
        return startPositions.get(servo);
    }

    private float getEndPosition(String servo)
    {
        return endPositions.get(servo);
    }
}
