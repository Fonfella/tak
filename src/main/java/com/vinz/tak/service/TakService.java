package com.vinz.tak.service;

import api.Discovery;
import api.ServoController;
import com.vinz.tak.model.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TakService extends AbstractService
{
    @Value("${tak.security.token:allerg}")
    public String SECURITY_TOKEN;

    @Value("${tak.servo.id.a:a}")
    public String SERVO_ID_A;

    @Value("${tak.servo.id.b:b}")
    public String SERVO_ID_B;

    @Autowired
    private Discovery discovery;

    private ServoController servoController;

    @PostConstruct
    public void init()
    {
        servoController = discovery.discover();
    }

    public void tak(Command command)
    {
        validateServo(command);

        execute(command);
    }

    private void execute(Command command)
    {
        //servoController
    }

    private void validateServo(Command command)
    {
        String servo = command.getServo().toLowerCase();

        if (servo.equals(SERVO_ID_A)) {

            return;
        }

        if (servo.equals(SERVO_ID_B)) {

            return;
        }

        exceptions.BadRequest("Invalid servo id [field: 'servo']");
    }
}
