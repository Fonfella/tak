package maestro;

import api.ServoController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pololu.maestro.PololuMaestroServoCard;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MaestroServoController implements ServoController
{

    @Value("${tak.servo.acceleration:50}")
    private short SERVO_ACCELERATION;

    @Value("${tak.servo.speed:50}")
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

    public void tak(String servoId, long wait)
    {
        short channel = idToChannel(servoId);

        initChannel(channel);

        moveDown(channel);
        doWait(wait);
        moveUp(channel);
    }

    private void moveUp(short channel)
    {

    }

    private void doWait(long wait)
    {

    }

    private void moveDown(short channel)
    {

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
