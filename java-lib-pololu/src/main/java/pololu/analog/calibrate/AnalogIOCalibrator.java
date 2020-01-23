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
package pololu.analog.calibrate;

import pololu.analog.AnalogIO;
import pololu.util.sguava.Converter;

/**
 * Calibrates a single {@link AnalogIO}.
 *
 * @author Greg Elderfield
 */
public interface AnalogIOCalibrator
{

    /**
     * @return A converter that calibrates output values (the forward conversion) and input values (the reverse conversion).
     */
    Converter<Double, Double> getValueConverter();

}
