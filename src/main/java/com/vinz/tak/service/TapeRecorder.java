package com.vinz.tak.service;

import com.vinz.tak.model.ExecResult;
import com.vinz.tak.model.ProcessOptions;
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
import java.util.concurrent.Future;
import java.util.function.Predicate;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;
import static org.springframework.util.StringUtils.isEmpty;

@Component
@Scope(SCOPE_PROTOTYPE)
public class TapeRecorder extends AbstractService {

    private final StartRecord startRecord;

    @Autowired
    private AdbExecutor adbExecutor;

    @Autowired
    private EventFactory eventFactory;

    @Autowired
    private TapeCodec tapeCodec;

    public TapeRecorder(StartRecord start) {

        startRecord = start;
    }

    private String getCompressed(ExecResult result) throws IOException {

        List<Event> events = eventFactory.parse(result.getLines());

        if (events.isEmpty()) {

            return null;
        }

        return tapeCodec.compress(events);
    }

    @Async
    public Future<Tape> record() throws IOException {

        ExecResult result = getEvents(startRecord);

        Tape.TapeBuilder tapeBuilder = Tape.builder();

        tapeBuilder.did(startRecord.getDid());
        tapeBuilder.data(getCompressed(result));

        return new AsyncResult<>(tapeBuilder.build());
    }

    private Predicate<String> getEventFilter() {

        return s -> s.startsWith("/dev/input/event");
    }

    public ExecResult getEvents(StartRecord startRecord) {

        ProcessOptions options = new ProcessOptions();
        options.setFilter(getEventFilter());
        options.setTimeout(startRecord.getWait());

        String did = startRecord.getDid();

        if (!isEmpty(did)) {

            return adbExecutor.adb(options, "-s", did, "shell", "getevent");

        } else {

            return adbExecutor.adb(options, "shell", "getevent");
        }
    }
}
