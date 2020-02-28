package com.vinz.tak.service;

import com.vinz.tak.model.ProcessOptions;
import com.vinz.tak.model.Tape;
import com.vinz.tak.util.ProcessUtils;
import com.vinz.tape.factory.EventFactory;
import com.vinz.tape.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;
import static org.springframework.util.StringUtils.isEmpty;

@Component
@Scope(SCOPE_PROTOTYPE)
public class TapePlayer extends AbstractService {

    @Autowired
    private AdbExecutor adbExecutor;

    @Autowired
    private EventFactory eventFactory;

    @Autowired
    private ProcessUtils processUtils;

    public Tape play(List<Event> events, String did) {

        ProcessOptions options = new ProcessOptions();

        for (Event event : events) {

            playEvent(options, event, did);
        }

        Tape.TapeBuilder builder = Tape.builder();

        builder.did(did);

        return builder.build();
    }

    private void playEvent(ProcessOptions options, Event event, String did) {

        String[] params = eventFactory.explode(event);

        String[] sendevent = processUtils.prepender(params, "shell", "sendevent");

        if (!isEmpty(did)) {

            sendevent = processUtils.prepender(sendevent, "-d", did);
        }

        adbExecutor.adb(options, sendevent);
    }
}
