package com.fonfella.pololu.maestro;

import com.fonfella.pololu.api.ServoController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pololu.maestro.PololuMaestroServoCard;
import pololu.usb.exception.UsbRuntimeException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class MaestroServoController implements ServoController
{
    private static Map<String, Short> servoChannels = new HashMap<>();

    static {
        servoChannels.put("a", (short) 0);
        servoChannels.put("b", (short) 1);
        servoChannels.put("c", (short) 2);
        servoChannels.put("d", (short) 3);
    }

    private PololuMaestroServoCard servoCard;

    public MaestroServoController(PololuMaestroServoCard card)
    {
        servoCard = card;
    }

    @Override
    public void raw(String servoId, float position, short speed, short acceleration)
    {
        short channel = idToChannel(servoId);

        servoCard.setSpeed(channel, speed);
        servoCard.setAcceleration(channel, acceleration);

        short pos = mapToServoUs(position);
        servoCard.setPosition(channel, pos);
    }

    @Override
    public boolean test()
    {
        try {

            servoCard.getPosition((short) 0);

        } catch (UsbRuntimeException ure) {

            return false;
        }

        return true;
    }

    private short mapToServoUs(float position)
    {
        return (short) Math.round((position * 1000 + 1000) * 4);
    }

    private short idToChannel(String servoId)
    {
        return servoChannels.get(servoId);
    }
}
