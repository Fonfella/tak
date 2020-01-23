/**
 * IAmContent Public Libraries.
 * Copyright (C) 2015 Greg Elderfield
 *
 * @author Greg Elderfield, support@jarchitect.co.uk
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package pololu.maestro.card;

import pololu.maestro.PololuMaestroServoCard;
import pololu.usb.Usb;

import javax.usb.UsbDevice;
import java.util.function.Predicate;

/**
 * A factory for {@link AbstractPololuMaestroServoCard}s.
 *
 * @author Greg Elderfield
 */
public class PololuMaestroServoCards
{

    /**
     * Creates an instance with the first Pololu Maestro {@link UsbDevice} that is found.
     */
    public static PololuMaestroServoCard defaultUsbPololuMaestroServoCard()
    {
        return usbPololuMaestroServoCard(Usb.device(isAMaestroUsbDevice()));
    }

    /**
     * Creates an instance with the given UsbDevice.
     */
    public static PololuMaestroServoCard usbPololuMaestroServoCard(UsbDevice device)
    {

        final MaestroCardType cardType = MaestroCardType.forUsbDeviceOrThrow(device);

        switch (cardType) {

            case USB_MICRO_MAESTRO_6:
                return new PololuMicroMaestroServoCard(device);

            default:
                return new PololuMiniMaestroServoCard(device);
        }
    }

    private static Predicate<UsbDevice> isAMaestroUsbDevice()
    {
        return MaestroCardType::isAMaestroCard;
    }
}
