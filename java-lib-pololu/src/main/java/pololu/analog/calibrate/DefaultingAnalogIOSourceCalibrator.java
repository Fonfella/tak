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

import pololu.util.sguava.Converter;

import java.util.Collections;
import java.util.Map;

import static pololu.util.sguava.Sguava.checkNotNull;

/**
 * A {@link AnalogIOSourceCalibrator} that allows a default {@link Converter} to be set but over-ridden on a per-channel basis.
 *
 * @param <C> The type used to identify a channel.
 * @author Greg Elderfield
 */
public class DefaultingAnalogIOSourceCalibrator<C> implements AnalogIOSourceCalibrator<C>
{

    private final AnalogIOCalibrator defaultCalibrator;
    private final Map<C, AnalogIOCalibrator> perChannelCalibrators;

    public DefaultingAnalogIOSourceCalibrator(AnalogIOCalibrator defaultCalibrator)
    {
        this(defaultCalibrator, Collections.<C, AnalogIOCalibrator>emptyMap());
    }

    public DefaultingAnalogIOSourceCalibrator(AnalogIOCalibrator defaultCalibrator, Map<C, AnalogIOCalibrator> perChannelCalibrators)
    {
        checkNotNull(defaultCalibrator, "The default calibrator cannot be null.");
        checkNotNull(perChannelCalibrators, "The per-channel calibrator Map cannot be null, although it may be empty.");

        this.defaultCalibrator = defaultCalibrator;
        this.perChannelCalibrators = perChannelCalibrators;
    }

    @Override
    public AnalogIOCalibrator forChannel(C channel)
    {
        final AnalogIOCalibrator c = perChannelCalibrators.get(channel);

        return c != null ? c : defaultCalibrator;
    }
}
