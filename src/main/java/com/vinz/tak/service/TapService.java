package com.vinz.tak.service;

import com.vinz.tak.model.StartRecord;
import com.vinz.tak.model.Tape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public abstract class TapService extends AbstractService {

    public static final String DID_DEFAULT = "DEFAULT";

    @Autowired
    @Qualifier("adbTaskExecutor")
    public AsyncTaskExecutor taskExecutor;

    private ConcurrentHashMap<String, TapeDeck> tapeDecks = new ConcurrentHashMap<>();

    public Tape play(Tape tape) throws ExecutionException, InterruptedException, IOException {

        try {

            TapeDeck recorder = acquireTapeRecorder(tape.getDid());

            return recorder.play(tape).get();

        } finally {

            releaseTapeRecorder(tape.getDid());
        }
    }

    public Tape record(StartRecord startRecord) throws ExecutionException, InterruptedException, IOException {

        try {

            TapeDeck recorder = acquireTapeRecorder(startRecord.getDid());

            return recorder.record(startRecord).get();

        } finally {

            releaseTapeRecorder(startRecord.getDid());
        }
    }

    private void releaseTapeRecorder(String did) {

        if (isEmpty(did)) {

            did = DID_DEFAULT;
        }

        tapeDecks.remove(did);
    }

    private TapeDeck acquireTapeRecorder(String did) {

        if (isEmpty(did)) {

            did = DID_DEFAULT;
        }

        if (tapeDecks.containsKey(did)) {

            throw new RuntimeException("Device ID " + did + " is busy in another recording");
        }

        TapeDeck recorder = getRecorder();

        tapeDecks.put(did, recorder);

        return recorder;
    }

    @Lookup
    public abstract TapeDeck getRecorder();
}
