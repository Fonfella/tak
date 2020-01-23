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
package pololu.servo.command;

import pololu.servo.ServoSource;

/**
 * A simple {@link ServoCommandExecutor}.
 *
 * @param <C> The type used to identify the channel of a servo.
 * @author Greg Elderfield
 */
public class SimpleServoCommandExecutor<C> implements ServoCommandExecutor<C>
{

    private final ServoSource<C> servoSource;

    public static <C> SimpleServoCommandExecutor<C> executor(ServoSource<C> servoSource)
    {
        return new SimpleServoCommandExecutor<C>(servoSource);
    }

    public SimpleServoCommandExecutor(ServoSource<C> servoSource)
    {
        this.servoSource = servoSource;
    }

    @Override
    public void execute(ServoCommand<? extends C> command)
    {
        final C channel = command.getChannel();
        setAcceleration(channel, command.getAcceleration());
        setSpeed(channel, command.getSpeed());
        setValue(channel, command.getValue());
    }

    private void setAcceleration(C channel, Double acceleration)
    {
        if (acceleration != null) {
            servoSource.forChannel(channel).setAcceleration(acceleration);
        }
    }

    private void setSpeed(C channel, Double speed)
    {
        if (speed != null) {
            servoSource.forChannel(channel).setSpeed(speed);
        }
    }

    private void setValue(C channel, Double value)
    {
        if (value != null) {
            servoSource.forChannel(channel).setValue(value);
        }
    }
}
