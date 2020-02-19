package com.vinz.tape.codec;

import com.vinz.tape.model.Event;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

@Component
public class TapeCodec {

    public String compress(List<Event> events) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        DeflaterOutputStream def = new DeflaterOutputStream(baos, new Deflater(Deflater.BEST_COMPRESSION));

        DataOutputStream dos = new DataOutputStream(def);

        for (Event event : events) {

            serializeEvent(dos, event);
        }

        def.close();
        baos.close();

        return baToString(baos.toByteArray());
    }

    public List<Event> decompress(String data) throws IOException {

        byte[] binary = stringToBa(data);

        DataInputStream dataInputStream = new DataInputStream(new InflaterInputStream(new ByteArrayInputStream(binary)));

        List<Event> events = new ArrayList<>();

        while (dataInputStream.available() > 0) {

            events.add(unserializeEvent(dataInputStream));
        }

        return events;
    }

    private Event unserializeEvent(DataInputStream dataInputStream) throws IOException {

        Event.EventBuilder builder = Event.builder();

        builder.device(dataInputStream.readInt());
        builder.command(dataInputStream.readInt());
        builder.argument(dataInputStream.readInt());
        builder.value(dataInputStream.readInt());

        return builder.build();
    }

    private void serializeEvent(DataOutputStream dos, Event event) throws IOException {

        dos.writeInt(event.getDevice());
        dos.writeInt(event.getCommand());
        dos.writeInt(event.getArgument());
        dos.writeInt(event.getValue());
    }

    private String baToString(byte[] data) throws UnsupportedEncodingException {

        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] stringToBa(String data) {

        return Base64.getDecoder().decode(data);
    }
}
