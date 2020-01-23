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

import pololu.analog.AnalogIO;
import pololu.servo.Servo;
import pololu.servo.calibrate.ServoSourceCalibrator;

import java.util.function.Function;

/**
 * The functions and calibrators required to support a 'standard' configuration of {@link Servo}s, where each raw {@link Servo} is calibrated to a normal range (0..1), then
 * re-channeled and then calibrated once more (tuned), or some subset of this configuration.
 *
 * @param <R> The type used to identify the channel of a raw {@link Servo}.
 * @param <C> The type used to identify the channel of a rechanneled {@link Servo}.
 * @author Greg Elderfield
 */
public interface ServoConfigFunctions<R, C>
{
    /**
     * @return A Calibrator that calibrates the raw {@link AnalogIO}s to a normal range (0..1).
     */
    ServoSourceCalibrator<R> normalizingCalibrator();

    /**
     * @return A function that translates from {@link C} values to the corresponding {@link R values.
     */
    Function<C, R> channelTranslation();

    /**
     * @return A Calibrator that tunes the rechanneled, normalized {@link AnalogIO}s to their desired range.
     */
    ServoSourceCalibrator<C> tuningCalibrator();

}
