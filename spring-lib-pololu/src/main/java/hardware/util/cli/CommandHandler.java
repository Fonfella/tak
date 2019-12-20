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
package hardware.util.cli;

import java.util.function.Predicate;

import static hardware.util.cli.CommandLineDriver.tidied;

/**
 * An abstract class for implementing command handlers (i.e. Predicates which execute any command that they
 * can understand, returning true when a command is executed, false otherwise).
 *
 * @author Greg Elderfield
 */
public abstract class CommandHandler implements Predicate<String>
{

    @Override
    public boolean test(String command)
    {
        try {
            return executeIfCommandMatches(tidied(command));
        } catch (Exception e) {
            System.out.println("Cound not execute command: '" + command + "': " + e.getMessage());
            return true;
        }
    }

    private boolean executeIfCommandMatches(String command)
    {
        if (isMatchingCommand(command)) {
            executeCommand(commandArgument(command));
            return true;
        }
        return false;
    }

    protected String commandArgument(String command)
    {
        return command;
    }

    protected abstract boolean isMatchingCommand(String command);

    protected abstract void executeCommand(String argument);
}