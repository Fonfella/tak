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

import static com.vinz.tak.service.TapeDeck.DEVICE_OPTION;
import static com.vinz.tape.factory.EventFactory.EVENT_PREFIX;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;
import static org.springframework.util.StringUtils.isEmpty;

@Component
@Scope(SCOPE_PROTOTYPE)
public class TapeRecorder extends AbstractService {

    @Autowired
    private AdbExecutor adbExecutor;

    private Predicate<String> getEventFilter() {

        return s -> s.startsWith(EVENT_PREFIX);
    }

    public List<String> record(StartRecord startRecord) {

        ProcessOptions options = new ProcessOptions();
        options.setFilter(getEventFilter());
        options.setTimeout(startRecord.getWait());

        String did = startRecord.getDid();

        ExecResult result;

        if (!isEmpty(did)) {

            result = adbExecutor.adb(options, DEVICE_OPTION, did, "shell", "getevent");

        } else {

            result = adbExecutor.adb(options, "shell", "getevent");
        }

        return result.getLines();
    }
}
