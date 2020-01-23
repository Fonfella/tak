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
package pololu.servo.config;

import pololu.servo.Servo;
import pololu.servo.ServoSource;
import pololu.servo.calibrate.ServoSourceCalibrator;
import pololu.servo.raw.ServoController;

import java.util.function.Function;

import static pololu.servo.ServoSources.*;
import static pololu.util.sguava.Sguava.checkNotNull;

/**
 * A 'standard' configuration of {@link Servo}s, where each raw {@link Servo} is calibrated to a normal range (0..1), then
 * re-channeled and then calibrated once more (tuned), or some subset of this configuration.
 *
 * @author Greg Elderfield
 *
 * @param <R> The type used to identify the channel of a raw {@link Servo}. 
 * @param <C> The type used to identify the channel of a rechanneled {@link Servo}. 
 */
public class ServoConfiguration<R, C>
{

    private final ServoController<R> controller;
    private final ServoSource<R> rawServos;
    private final ServoSource<R> normalServos;
    private final ServoSource<C> normalRechanneledServos;
    private final ServoSource<C> tunedRechanneledServos;

    public ServoConfiguration(ServoController<R> servoController, ServoConfigFunctions<R, C> configFunctions)
    {
        this(servoController, configFunctions.normalizingCalibrator(), configFunctions.channelTranslation(), configFunctions.tuningCalibrator());
    }

    public ServoConfiguration(ServoController<R> servoController, ServoSourceCalibrator<R> normalizingServoCalibrator,
                              Function<C, R> channelTranslation, ServoSourceCalibrator<C> tuningCalibrator)
    {

        tunedRechanneledServos =
                calibratedServoSource(
                        normalRechanneledServos = reChanneledServoSourceOrNull(
                                normalServos = calibratedServoSourceOrNull(
                                        rawServos = rawServoSource(controller = servoController),
                                        normalizingServoCalibrator),
                                channelTranslation),
                        tuningCalibrator);
    }

    public ServoConfiguration(ServoController<R> servoController, ServoSourceCalibrator<R> normalizingServoCalibrator,
                              Function<C, R> channelTranslation)
    {
        this(servoController, normalizingServoCalibrator, channelTranslation, null);
    }

    public ServoConfiguration(ServoController<R> servoController, ServoSourceCalibrator<R> normalizingServoCalibrator)
    {
        this(servoController, normalizingServoCalibrator, null, null);
    }

    public ServoController<R> getController()
    {
        return checkNotNull(controller);
    }

    public ServoSource<R> getRawServos()
    {
        return checkNotNull(rawServos);
    }

    public ServoSource<R> getNormalServos()
    {
        return checkNotNull(normalServos);
    }

    public ServoSource<C> getNormalRechanneledServos()
    {
        return checkNotNull(normalRechanneledServos);
    }

    public ServoSource<C> getTunedRechanneledServos()
    {
        return checkNotNull(tunedRechanneledServos);
    }

    private static <C> ServoSource<C> calibratedServoSourceOrNull(ServoSource<C> servoSource, ServoSourceCalibrator<C> tuningCalibrator)
    {
        if (tuningCalibrator == null) {
            return null;
        }
        return calibratedServoSource(servoSource, tuningCalibrator);
    }

    private static <R, C> ServoSource<C> reChanneledServoSourceOrNull(ServoSource<R> servoSource, Function<C, R> channelTranslation)
    {
        if (channelTranslation == null) {
            return null;
        }
        return reChanneledServoSource(servoSource, channelTranslation);
    }
}
