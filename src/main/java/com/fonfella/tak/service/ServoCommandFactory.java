package com.fonfella.tak.service;

import com.fonfella.tak.model.Command;
import com.fonfella.tak.model.ServoCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ServoCommandFactory
{
    @Value("${tak.servo.id.a:a}")
    public String SERVO_ID_A;

    @Value("${tak.servo.id.b:b}")
    public String SERVO_ID_B;

    @Value("${tak.servo.id.c:c}")
    public String SERVO_ID_C;

    @Value("${tak.servo.id.d:d}")
    public String SERVO_ID_D;

    @Value("${tak.servo.a.start:0.0}")
    private float SERVO_A_START;

    @Value("${tak.servo.b.start:1.0}")
    private float SERVO_B_START;

    @Value("${tak.servo.c.start:0.0}")
    private float SERVO_C_START;

    @Value("${tak.servo.d.start:1.0}")
    private float SERVO_D_START;

    @Value("${tak.servo.a.end:1.0}")
    private float SERVO_A_END;

    @Value("${tak.servo.b.end:0.0}")
    private float SERVO_B_END;

    //verificare inclinazione servo per posGrande
    @Value("${tak.servo.c.end:1.0}")
    private float SERVO_C_END;

    //verificare inclinazione servo per posGrande
    @Value("${tak.servo.d.end:0.0}")
    private float SERVO_D_END;

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
        //AGGIUNTI C e D START
        startPositions.put(SERVO_ID_C, SERVO_C_START);
        startPositions.put(SERVO_ID_D, SERVO_D_START);

        endPositions.put(SERVO_ID_A, SERVO_A_END);
        endPositions.put(SERVO_ID_B, SERVO_B_END);
        //AGGIUNTI C e D END
        endPositions.put(SERVO_ID_C, SERVO_C_END);
        endPositions.put(SERVO_ID_D, SERVO_D_END);
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
