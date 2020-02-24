package com.vinz.tak.service;

import com.vinz.tak.model.ExecResult;
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
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class TapRecorder extends AbstractService implements Runnable {

    @Autowired
    private AdbExecutor adbExecutor;

    @Autowired
    private EventFactory eventFactory;

    @Autowired
    private TapeCodec tapeCodec;

    public Tape record(StartRecord startRecord) throws IOException, ExecutionException, InterruptedException {

        Future<Tape> tapeFuture = startRecord(startRecord);

        return tapeFuture.get();
    }

    @Async
    public Future<Tape> startRecord(StartRecord startRecord) throws IOException {

        ExecResult result = adbExecutor.getEvents(startRecord);

        if (result.getStatus() < 0 || result.getStatus() > 1) {

            throw new RuntimeException("Error executing process: [" + result.getStatus() + "] " + result.getMessage());
        }

        List<Event> events = eventFactory.parse(result.getLines());

        String compressed = tapeCodec.compress(events);

        Tape.TapeBuilder builder = Tape.builder();

        builder.did(startRecord.getDid());
        builder.data(compressed);

        return new AsyncResult<>(builder.build());
    }

    @Override
    public void run() {

    }
}
