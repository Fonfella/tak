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

    private ConcurrentHashMap<String, TapeRecorder> tapeRecorders = new ConcurrentHashMap<>();

    public Tape record(StartRecord startRecord) throws ExecutionException, InterruptedException, IOException {

        String did = startRecord.getDid();

        if (isEmpty(did)) {

            did = DID_DEFAULT;
        }

        if (tapeRecorders.containsKey(did)) {

            throw new RuntimeException("Device ID " + did + " is busy in another recording");
        }

        try {

            TapeRecorder recorder = getRecorder(startRecord);

            tapeRecorders.put(did, recorder);

            return recorder.record().get();

        } finally {

            tapeRecorders.remove(did);
        }
    }

    @Lookup
    public abstract TapeRecorder getRecorder(StartRecord startRecord);
}
