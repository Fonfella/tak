package maestro;

import api.ServoController;
import exception.PololuException;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;
import pololu.maestro.PololuMaestroServoCard;
import pololu.maestro.card.PololuMaestroServoCards;
import pololu.usb.exception.UsbRuntimeException;


@Component
public abstract class MaestroDiscovery extends AbstractDiscovery
{
    private PololuMaestroServoCard servoCard;

    @Override
    public ServoController discover()
    {
        if (servoCard == null) {

            scan();

            log.info("ServoCard found: " + servoCard.getType());
        }

        return getController(servoCard);
    }

    private void scan()
    {
        try {

            servoCard = PololuMaestroServoCards.defaultUsbPololuMaestroServoCard();

        } catch (UsbRuntimeException ure) {

            throw new PololuException("Cannot find a Pololu connected card via USB. " + ure.getMessage());
        }
    }


    @Lookup
    public abstract ServoController getController(PololuMaestroServoCard servoCard);
}
