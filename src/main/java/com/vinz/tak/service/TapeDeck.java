package com.vinz.tak.service;

import com.vinz.tak.model.StartRecord;
import com.vinz.tak.model.Tape;
import com.vinz.tape.codec.TapeCodec;
import com.vinz.tape.factory.EventFactory;
import com.vinz.tape.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;
import static org.springframework.util.StringUtils.isEmpty;

@Component
@Scope(SCOPE_PROTOTYPE)
public class TapeDeck extends AbstractService {

    public static final String DEVICE_OPTION = "-s";

    @Autowired
    private EventFactory eventFactory;

    @Autowired
    private TapeCodec tapeCodec;

    @Autowired
    private TapeRecorder tapeRecorder;

    @Autowired
    private TapePlayer tapePlayer;

    @Async
    public Future<Tape> record(StartRecord startRecord) throws IOException {

        List<String> events = tapeRecorder.record(startRecord);

        Tape.TapeBuilder tapeBuilder = Tape.builder();

        tapeBuilder.did(startRecord.getDid());
        tapeBuilder.data(deflate(eventFactory.parse(events)));

        return new AsyncResult<>(tapeBuilder.build());
    }

    @Async
    public Future<Tape> play(Tape tape) throws IOException {

        List<Event> events = inflate(tape.getData());

        Tape played = tapePlayer.play(events, tape.getDid());

        return new AsyncResult<>(played);
    }

    private String deflate(List<Event> events) throws IOException {

        if (events == null || events.isEmpty()) {

            return null;
        }

        return tapeCodec.compress(events);
    }

    private List<Event> inflate(String data) throws IOException {

        if (isEmpty(data)) {

            return Collections.emptyList();
        }

        return tapeCodec.decompress(data);
    }
}
