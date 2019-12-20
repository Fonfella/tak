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

import com.google.common.collect.Iterables;
import com.iamcontent.io.util.BufferedReaderIterator;
import hardware.util.io.BufferedReaderIterator;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.or;
import static com.iamcontent.io.util.Readers.bufferedReader;
import static hardware.util.io.Readers.bufferedReader;

/**
 * Foundation class for Command Line Interface (CLI) drivers.
 * @author Greg Elderfield
 */
public abstract class CommandLineDriver
{


    protected final BufferedReader in;
    private final List<Predicate<String>> commandHandlers = new ArrayList<>();

    public CommandLineDriver()
    {
        this(bufferedReader(System.in));
        addCommandHandler(quitCommandHandler());
    }

    public CommandLineDriver(BufferedReader in)
    {
        this.in = in;
    }

    public static String tidied(String command)
    {
        return command.trim().toLowerCase();
    }

    protected Iterable<String> commandStrings()
    {

        return Iterables.filter(inputLines(), commandShouldBeProcessed());
    }

    private BufferedReaderIterator inputLines()
    {
        return new BufferedReaderIterator(in)
        {
            @Override
            public String next()
            {
                System.out.print("> ");
                System.out.flush();
                return super.next();
            }
        };
    }

    protected Predicate<String> commandShouldBeProcessed()
    {
        return not(commandExecutedByAnyHandler());
    }

    private Predicate<String> commandExecutedByAnyHandler()
    {
        return or(commandHandlers);
    }

    protected void onQuit()
    {
    }

    protected void addCommandHandler(Predicate<String> handler)
    {
        commandHandlers.add(handler);
    }

    private CommandHandler quitCommandHandler()
    {
        return new LiteralCommandHandler(null, "q", "quit", "exit", "bye")
        {
            @Override
            protected void executeCommand(String argument)
            {
                onQuit();
                System.out.println("Bye");
                System.exit(0);
            }
        };
    }
}
