package com.fonfella.pololu.maestro;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fonfella.pololu.api.ServoController;
import pololu.maestro.PololuMaestroServoCard;
import pololu.maestro.card.PololuMaestroServoCards;
import pololu.usb.exception.UsbRuntimeException;


@Component
public abstract class MaestroDiscovery extends AbstractDiscovery
{
    private PololuMaestroServoCard servoCard;

    @Value("${pololu.scan.waitMillis:1000}")
    private long DISCOVER_WAIT;

    @Override
    public ServoController discover()
    {
        scan();

        log.info("ServoCard found: " + servoCard.getType());

        return getController(servoCard);
    }

    @Override
    public void clear()
    {
        servoCard = null;
    }

    private void scan()
    {
        while (servoCard == null)
        {
            try
            {
                servoCard = PololuMaestroServoCards.defaultUsbPololuMaestroServoCard();
            }
            catch (UsbRuntimeException ure)
            {
                log.info("Cannot find a Pololu Maestro card: " + ure.getMessage());
                log.info("Wait for device " + DISCOVER_WAIT + " milliseconds");

                try
                {
                    Thread.sleep(DISCOVER_WAIT);
                }
                catch (InterruptedException ignored)
                {
                    clear();
                }
            }
        }
    }

    @Lookup
    public abstract ServoController getController(PololuMaestroServoCard servoCard);
}
