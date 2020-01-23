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
import pololu.maestro.PololuMaestroServoCard;
import pololu.usb.EasyUsbDevice;

import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import java.io.Serializable;

import static pololu.usb.EasedUsbDevice.eased;

/**
 * An abstract {@link PololuMaestroServoCard} that uses Pololu's Native USB protocol.
 * Methods with implementations that vary by card type must be provided by concrete subclasses.
 * <p>
 * The command values used by this protocol were obtained from Pololu's Native USB SDK,
 * downloaded from www.pololu.com.
 *
 * @author Greg Elderfield
 */
public abstract class AbstractPololuMaestroServoCard implements PololuMaestroServoCard
{

    private static final byte REQUEST_SET_TARGET = (byte) 0x85;
    private static final byte REQUEST_SET_SERVO_VARIABLE = (byte) 0x84;

    private static final int BYTES_PER_SERVO_STATUS_BLOCK = 7;

    private final EasyUsbDevice device;
    private final MaestroCardType type;
    private final byte[] dataIn;
    private final byte inRequestCode;
    private final int offsetOfFirstServoStatusBlock;

    public AbstractPololuMaestroServoCard(UsbDevice device, byte inRequestCode, int offsetOfFirstServoStatusBlock)
    {

        this.device = eased(device);
        this.type = MaestroCardType.forUsbDeviceOrThrow(device);
        this.inRequestCode = inRequestCode;
        this.offsetOfFirstServoStatusBlock = offsetOfFirstServoStatusBlock;
        this.dataIn = new byte[offsetOfFirstServoStatusBlock + sizeOfAllServoStatusBlocks()];
    }

    @Override
    public void setPosition(short channel, short position)
    {

        device.syncSubmit(outRequest(REQUEST_SET_TARGET, channel, position));
    }

    @Override
    public void setSpeed(short channel, short speed)
    {

        device.syncSubmit(outRequest(REQUEST_SET_SERVO_VARIABLE, channel, speed));
    }

    @Override
    public void setAcceleration(short channel, short acceleration)
    {

        final int accelerationFlag = 0x80;

        device.syncSubmit(
                outRequest(
                        REQUEST_SET_SERVO_VARIABLE,
                        (short) (
                                channel | accelerationFlag)
                        , acceleration));
    }

    @Override
    public short getPosition(short channel)
    {
        return getState().getPosition(channel);
    }

    @Override
    public State getState()
    {

        final UsbControlIrp request = inRequest(inRequestCode);

        device.syncSubmit(request);

        return new PololuState(request.getData());
    }

    @Override
    public MaestroCard getType()
    {
        return type.getType();
    }

    protected int channelCount()
    {
        return getType().channelCount();
    }

    protected UsbControlIrp inRequest(byte request)
    {
        final byte requestType = UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_DIRECTION_IN;
        final UsbControlIrp result = device.createUsbControlIrp(requestType, request, (short) 0, (short) 0);

        result.setData(dataIn);

        return result;
    }

    protected UsbControlIrp outRequest(byte request, short index, short value)
    {
        final byte requestType = UsbConst.REQUESTTYPE_TYPE_VENDOR | UsbConst.REQUESTTYPE_DIRECTION_OUT;

        return device.createUsbControlIrp(requestType, request, value, index);
    }

    protected int sizeOfAllServoStatusBlocks()
    {
        return channelCount() * BYTES_PER_SERVO_STATUS_BLOCK;
    }

    protected static short asShort(byte lo, byte hi)
    {
        return (short) ((hi << 8) | (lo & 0xFF));
    }

    protected class PololuState implements State, Serializable
    {

        private final byte[] data;

        public PololuState(byte[] data)
        {
            this.data = data;
        }

        @Override
        public short getPosition(short channel)
        {
            final int loIndex = offsetOfFirstServoStatusBlock + channel * BYTES_PER_SERVO_STATUS_BLOCK;

            return asShort(data[loIndex], data[loIndex + 1]);
        }

        @Override
        public String toString()
        {
            final short lastIndex = (short) (channelCount() - 1);
            final StringBuilder builder = new StringBuilder();

            builder.append("PololuState [positions=[");

            for (short i = 0; i < lastIndex; i++) {

                builder.append(getPosition(i)).append(", ");
            }

            builder.append(getPosition(lastIndex)).append("]]");

            return builder.toString();
        }

        private static final long serialVersionUID = 1L;
    }
}
