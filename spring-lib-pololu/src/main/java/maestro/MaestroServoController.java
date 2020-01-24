package maestro;

import api.ServoController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pololu.maestro.PololuMaestroServoCard;
import pololu.usb.exception.UsbRuntimeException;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MaestroServoController implements ServoController
{
    @Value("${tak.servo.acceleration:200}")
    private short SERVO_ACCELERATION;

    @Value("${tak.servo.speed:400}")
    private short SERVO_SPEED;

    private static Map<String, Short> servoChannels = new HashMap<>();

    static {
        servoChannels.put("a", (short) 0);
        servoChannels.put("b", (short) 1);
    }

    private PololuMaestroServoCard servoCard;

    public MaestroServoController(PololuMaestroServoCard card)
    {
        servoCard = card;
    }

    @Override
    public void set(String servoId, float position)
    {
        short channel = idToChannel(servoId);

        initChannel(channel);

        short pos = mapToServoUs(position);
        servoCard.setPosition(channel, pos);
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

    private void initChannel(short channel)
    {
        servoCard.setAcceleration(channel, SERVO_ACCELERATION);
        servoCard.setSpeed(channel, SERVO_SPEED);
    }

    private short idToChannel(String servoId)
    {
        return servoChannels.get(servoId);
    }
}
