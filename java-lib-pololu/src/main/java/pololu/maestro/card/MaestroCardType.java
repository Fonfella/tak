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


import pololu.maestro.MaestroCard;
import pololu.usb.exception.UsbRuntimeException;

import javax.usb.UsbDevice;

import static pololu.maestro.MaestroCard.*;
import static pololu.usb.UsbDevicePredicates.vendorAndProductIdsMatch;


/**
 * The USB characteristics of the different types of Pololu Maestro cards.
 * <p>
 * The id values used by this protocol were obtained from Pololu's Native USB SDK,
 * downloaded from www.pololu.com.
 *
 * @author Greg Elderfield
 */
public enum MaestroCardType
{
    USB_MICRO_MAESTRO_6(MAESTRO_MICRO6, 0x0089),
    USB_MINI_MAESTRO_12(MAESTRO_MINI12, 0x008A),
    USB_MINI_MAESTRO_18(MAESTRO_MINI18, 0x008B),
    USB_MINI_MAESTRO_24(MAESTRO_MINI24, 0x008C);

    public static final short VENDOR_ID = 0x1ffb;

    private final MaestroCard type;
    private final short productId;

    MaestroCardType(MaestroCard type, int productId)
    {
        this.type = type;
        this.productId = (short) productId;
    }

    public MaestroCard getType()
    {
        return type;
    }

    public short getProductId()
    {
        return productId;
    }

    public static MaestroCardType forUsbDeviceOrThrow(UsbDevice device)
    {
        final MaestroCardType result = forUsbDeviceOrNull(device);

        if (result != null) {

            return result;
        }

        throw new UsbRuntimeException("USB Device is not a Usb Maestro Card");
    }

    public static MaestroCardType forUsbDeviceOrNull(UsbDevice device)
    {
        for (MaestroCardType cardType : values()) {

            if (vendorAndProductIdsMatch(VENDOR_ID, cardType.getProductId(), device))

                return cardType;
        }

        return null;
    }

    public static boolean isAMaestroCard(UsbDevice device)
    {
        return forUsbDeviceOrNull(device) != null;
    }
}