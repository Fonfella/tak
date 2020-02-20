package com.vinz.tak.service;

import com.vinz.tak.model.ExecResult;
import com.vinz.tak.model.StartRecord;
import com.vinz.tak.model.Tape;
import com.vinz.tape.codec.TapeCodec;
import com.vinz.tape.factory.EventFactory;
import com.vinz.tape.model.Event;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class TapService extends AbstractService {

    @Autowired
    private AdbExecutor adbExecutor;

    @Autowired
    private EventFactory eventFactory;

    @Autowired
    private TapeCodec tapeCodec;

    @SneakyThrows
    public Tape record(StartRecord startRecord) {

        ExecResult result = adbExecutor.getEvents();

        if (result.getStatus() != 0) {

            throw new RuntimeException("Error executing process: [" + result.getStatus() + "] " + result.getMessage());
        }

        List<Event> events = eventFactory.parse(result.getLines());

        String compressed = tapeCodec.compress(events);

        Tape.TapeBuilder builder = Tape.builder();

        builder.did(startRecord.getDid());
        builder.data(compressed);

        return builder.build();
    }
}
