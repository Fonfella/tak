package com.fonfella.tape.factory;

import com.fonfella.tape.model.Event;
import com.fonfella.tape.model.RawEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EventFactory {

    public static final String EVENT_PREFIX = "/dev/input/event";

    private final Pattern number = Pattern.compile("\\d+");

    public List<Event> parse(List<RawEvent> lines) {

        if (lines == null || lines.isEmpty()) {

            return Collections.emptyList();
        }

        List<Event> events = new ArrayList<>();

        long previousEventTime = lines.get(0).getTimestamp();

        for (RawEvent line : lines) {

            Event event = parseLine(line);

            long timestamp = line.getTimestamp();

            event.setDelay(timestamp - previousEventTime);
            previousEventTime = timestamp;

            events.add(event);
        }

        return events;
    }

    private Event parseLine(RawEvent event) {

        String line = event.getLine();

        // /dev/input/event1: 0003 0039 0000003a
        Event.EventBuilder builder = Event.builder();

        String[] strings = line.split("\\s+");

        if (strings.length != 4) {

            throw new RuntimeException("Bad event line detected: [" + line + "]");
        }

        builder.device(parseDevice(strings[0]));
        builder.command(parseIntFromHex(strings[1]));
        builder.argument(parseIntFromHex(strings[2]));
        builder.value(parseLongFromHex(strings[3]));

        return builder.build();
    }

    private int parseIntFromHex(String data) {

        try {

            return Integer.parseInt(data.toUpperCase(), 16);

        } catch (NumberFormatException nfe) {

            throw new RuntimeException("Invalid event data: [" + data + "]");
        }
    }

    private long parseLongFromHex(String data) {

        try {

            return Long.parseLong(data.toUpperCase(), 16);

        } catch (NumberFormatException nfe) {

            throw new RuntimeException("Invalid event data: [" + data + "]");
        }
    }

    private int parseDevice(String deviceLine) {

        Matcher match = number.matcher(deviceLine);

        if (!match.find()) {

            throw new RuntimeException("Device ID is not a number: [" + deviceLine + "]");
        }

        String id = match.group();

        try {

            return Integer.parseInt(id);

        } catch (NumberFormatException nfe) {

            throw new RuntimeException("Device ID is not a number: [" + deviceLine + "]");
        }
    }

    public String[] explode(Event event) {

        // /dev/input/event1: 0003 0039 0000003a

        String[] strings = new String[5];

        strings[0] = "sendevent";
        strings[1] = EVENT_PREFIX + event.getDevice();
        strings[2] = String.valueOf(event.getCommand());
        strings[3] = String.valueOf(event.getArgument());
        strings[4] = String.valueOf(event.getValue());

        return strings;
    }
}
