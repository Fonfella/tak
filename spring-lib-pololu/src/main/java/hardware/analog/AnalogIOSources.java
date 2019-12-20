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
package hardware.analog;

import hardware.analog.calibrate.AnalogIOCalibrator;
import hardware.analog.calibrate.AnalogIOSourceCalibrator;
import hardware.analog.calibrate.CalibratedAnalogIO;

import java.util.function.Function;

/**
 * Methods to create {@link AnalogIOSource}s.
 *
 * @author Greg Elderfield
 */
public final class AnalogIOSources
{

    /**
     * @return An {@link AnalogIOSource} of {@link RawAnalogIO}s for the given {@link AnalogIOController}.
     */
    public static <C> AnalogIOSource<C> rawAnalogIOSource(final AnalogIOController<C> controller)
    {
        return channelId -> new RawAnalogIO<C>(controller, channelId);
    }

    /**
     * @return An {@link AnalogIOSource}, calibrated by applying the given calibration function to values before they are returned.
     */
    public static <C> AnalogIOSource<C> calibratedAnalogIOSource(final AnalogIOSource<C> delegate, final AnalogIOSourceCalibrator<C> calibration)
    {
        return channelId -> {

            final AnalogIO input = delegate.forChannel(channelId);
            final AnalogIOCalibrator calibrator = calibration.forChannel(channelId);

            return new CalibratedAnalogIO(input, calibrator);
        };
    }

    /**
     * @return An {@link AnalogIOSource} that reads values from a delegate {@link AnalogIOSource}, selecting inputs
     * from the latter according to the given channelTranslation function.
     */
    public static <C, D> AnalogIOSource<C> reChanneledAnalogIOSource(final AnalogIOSource<D> delegate, final Function<C, D> channelTranslation)
    {
        return channelId -> {

            final D delegateChannel = channelTranslation.apply(channelId);
            return delegate.forChannel(delegateChannel);
        };
    }
}
