package com.vinz.tape.factory;

import com.vinz.tape.model.Event;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventFactory {

    public List<Event> parse(List<String> lines) {

        List<Event> events = new ArrayList<>();

        for (String line : lines) {

            Event event = parseLine(line);

            events.add(event);
        }

        return events;
    }

    private Event parseLine(String line) {

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

        String id = deviceLine.substring(deviceLine.length() - 2, deviceLine.length() - 1);

        try {

            return Integer.parseInt(id);

        } catch (NumberFormatException nfe) {

            throw new RuntimeException("Device ID is not a number: [" + deviceLine + "]");
        }
    }
}
