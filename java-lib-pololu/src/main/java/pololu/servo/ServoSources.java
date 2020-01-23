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
package pololu.servo;

import pololu.servo.calibrate.CalibratedServo;
import pololu.servo.calibrate.ServoCalibrator;
import pololu.servo.calibrate.ServoSourceCalibrator;
import pololu.servo.raw.RawServo;
import pololu.servo.raw.ServoController;

import java.util.function.Function;

/**
 * Methods to facilitate {@link Servo} usage.
 * @author Greg Elderfield
 */
public final class ServoSources
{
    /**
     * @return A {@link ServoSource} of {@link RawServo}s for the given {@link ServoController}.
     */
    public static <C> ServoSource<C> rawServoSource(final ServoController<C> controller)
    {
        return new ServoSource<C>()
        {
            @Override
            public Servo forChannel(C channel)
            {
                return new RawServo<C>(controller, channel);
            }
        };
    }

    /**
     * @return A {@link ServoSource} of {@link CalibratedServo}s, calibrated by the given per-channel calibrator.
     * Each {@link Servo} from the returned source delegates to its corresponding {@link Servo} from the given {@link ServoSource}.
     */
    public static <C> ServoSource<C> calibratedServoSource(final ServoSource<C> delegate, final ServoSourceCalibrator<C> calibrator)
    {
        return new ServoSource<C>()
        {
            @Override
            public Servo forChannel(C channel)
            {
                final Servo delegateServo = delegate.forChannel(channel);
                final ServoCalibrator servoCalibrator = calibrator.forChannel(channel);
                return new CalibratedServo(delegateServo, servoCalibrator);
            }
        };
    }

    /**
     * @return A {@link ServoSource} that creates { ReChanneledServo}s from a delegate {@link ServoSource}, selecting {@link Servo}s
     * from the latter according to the given channelTranslation function.
     */
    public static <C, D> ServoSource<C> reChanneledServoSource(final ServoSource<D> delegate, final Function<C, D> channelTranslation)
    {
        return new ServoSource<C>()
        {
            @Override
            public Servo forChannel(C channel)
            {
                final D delegateChannel = channelTranslation.apply(channel);
                return delegate.forChannel(delegateChannel);
            }
        };
    }

    private ServoSources()
    {
    }
}
