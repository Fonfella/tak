/**
 * IAmContent Public Libraries.
 * Copyright (C) 2015 Greg Elderfield
 *
 * @author Greg Elderfield, iamcontent@jarchitect.co.uk
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
package pololu.analog.config;

import pololu.analog.AnalogIO;
import pololu.analog.AnalogIOController;
import pololu.analog.AnalogIOSource;
import pololu.analog.calibrate.AnalogIOSourceCalibrator;

import java.util.function.Function;

import static pololu.analog.AnalogIOSources.*;
import static pololu.util.sguava.Sguava.checkNotNull;

/**
 * A 'standard' configuration of {@link AnalogIO}s, where each raw {@link AnalogIO} is calibrated to a normal range (0..1), then
 * re-channeled and then calibrated once more (tuned), or some subset of this configuration.
 *
 * @param <R> The type used to identify the channel of a raw {@link AnalogIO}.
 * @param <C> The type used to identify the channel of a re-channeled {@link AnalogIO}.
 * @author Greg Elderfield
 */
public class AnalogIOConfiguration<R, C>
{

    private final AnalogIOController<R> controller;
    private final AnalogIOSource<R> rawIOs;
    private final AnalogIOSource<R> normalIOs;
    private final AnalogIOSource<C> normalRechanneledIOs;
    private final AnalogIOSource<C> tunedRechanneledIOs;

    public AnalogIOConfiguration(AnalogIOController<R> analogIOController, AnalogIOConfigFunctions<R, C> configFunctions)
    {
        this(analogIOController, configFunctions.normalizingCalibrator(), configFunctions.channelTranslation(), configFunctions.tuningCalibrator());
    }

    public AnalogIOConfiguration(AnalogIOController<R> analogIOController, AnalogIOSourceCalibrator<R> normalizingAnalogIOCalibrator,
                                 Function<C, R> channelTranslation, AnalogIOSourceCalibrator<C> tuningCalibrator)
    {

        tunedRechanneledIOs =
                calibratedAnalogIOSourceOrNull(
                        normalRechanneledIOs = reChanneledAnalogIOSourceOrNull(
                                normalIOs = calibratedAnalogIOSource(
                                        rawIOs = rawAnalogIOSource(controller = analogIOController),
                                        normalizingAnalogIOCalibrator),
                                channelTranslation),
                        tuningCalibrator);
    }

    public AnalogIOConfiguration(AnalogIOController<R> analogIOController, AnalogIOSourceCalibrator<R> normalizingAnalogIOCalibrator, Function<C, R> channelTranslation)
    {
        this(analogIOController, normalizingAnalogIOCalibrator, channelTranslation, null);
    }

    public AnalogIOConfiguration(AnalogIOController<R> analogIOController, AnalogIOSourceCalibrator<R> normalizingAnalogIOCalibrator)
    {
        this(analogIOController, normalizingAnalogIOCalibrator, null, null);
    }

    public AnalogIOController<R> getController()
    {
        return checkNotNull(controller);
    }

    public AnalogIOSource<R> getRawIOs()
    {
        return checkNotNull(rawIOs);
    }

    public AnalogIOSource<R> getNormalIOs()
    {
        return checkNotNull(normalIOs);
    }

    public AnalogIOSource<C> getNormalRechanneledIOs()
    {
        return checkNotNull(normalRechanneledIOs);
    }

    public AnalogIOSource<C> getTunedRechanneledIOs()
    {
        return checkNotNull(tunedRechanneledIOs);
    }

    private static <C> AnalogIOSource<C> calibratedAnalogIOSourceOrNull(AnalogIOSource<C> analogIOSource, AnalogIOSourceCalibrator<C> tuningCalibrator)
    {
        if (tuningCalibrator == null) {
            return null;
        }

        return calibratedAnalogIOSource(analogIOSource, tuningCalibrator);
    }

    private static <R, C> AnalogIOSource<C> reChanneledAnalogIOSourceOrNull(AnalogIOSource<R> analogIOSource, Function<C, R> channelTranslation)
    {
        if (channelTranslation == null) {
            return null;
        }

        return reChanneledAnalogIOSource(analogIOSource, channelTranslation);
    }
}
