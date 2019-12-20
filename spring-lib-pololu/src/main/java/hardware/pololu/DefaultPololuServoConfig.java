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
package hardware.pololu;

import hardware.servo.Servo;
import hardware.servo.ServoSource;
import hardware.servo.calibrate.ServoSourceCalibrator;
import hardware.servo.config.ServoConfigFunctions;
import hardware.servo.config.ServoConfiguration;
import hardware.servo.raw.ServoController;

import javax.usb.UsbDevice;
import java.util.function.Function;

import static hardware.pololu.PololuMaestroServoController.pololuMaestroServoController;
import static hardware.pololu.card.PololuMaestroServoCards.defaultUsbPololuMaestroServoCard;

/**
 * A helper for creating a default configuration of {@link Servo}s of the {@link PololuMaestroServoController},
 * calibrated to the normal (0..1) range. Uses the first Pololu Maestro {@link UsbDevice} that is found.
 *
 * @author Greg Elderfield
 */
public class DefaultPololuServoConfig
{

    public static ServoSource<Integer> normalServos()
    {

        return normalizingConfiguration().getNormalServos();
    }

    public static ServoConfiguration<Integer, Void> normalizingConfiguration()
    {
        return new ServoConfiguration<Integer, Void>(servoController(), configFunctions());
    }

    public static ServoController<Integer> servoController()
    {
        return pololuMaestroServoController(defaultUsbPololuMaestroServoCard());
    }

    public static ServoConfigFunctions<Integer, Void> configFunctions()
    {
        return new ServoConfigFunctions<Integer, Void>()
        {
            @Override
            public ServoSourceCalibrator<Integer> normalizingCalibrator()
            {
                return new DefaultPololuMaestroServoNormalization();
            }

            @Override
            public Function<Void, Integer> channelTranslation()
            {
                return null;
            }

            @Override
            public ServoSourceCalibrator<Void> tuningCalibrator()
            {
                return null;
            }
        };
    }
}
