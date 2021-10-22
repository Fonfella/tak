package com.fonfella.tak.service;

import com.fonfella.tak.model.ExecResult;
import com.fonfella.tak.model.ProcessOptions;
import com.fonfella.tak.model.StartRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

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

    public List<ExecResult.Line> record(StartRecord startRecord) {

        ProcessOptions options = new ProcessOptions();

        options.setFilter(getEventFilter());
        options.setTimeout(startRecord.getWait());

        String did = startRecord.getDid();

        ExecResult result;

        if (!isEmpty(did)) {

            result = adbExecutor.adb(options, TapeDeck.DEVICE_OPTION, did, "shell", "getevent");

        } else {

            result = adbExecutor.adb(options, "shell", "getevent");
        }

        return result.getLines();
    }
}
