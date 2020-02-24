package com.vinz.tak.service;

import com.vinz.tak.model.StartRecord;
import com.vinz.tak.model.Tape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;


@Component
public abstract class TapService extends AbstractService {

    @Autowired
    @Qualifier("adbExecutor")
    public TaskExecutor taskExecutor;

    public Tape record(StartRecord startRecord) {

        TapRecorder recorder = getRecorder("aaa");

        taskExecutor.execute(recorder);

        return null;
    }

    @Lookup
    public abstract TapRecorder getRecorder(String did);
}
