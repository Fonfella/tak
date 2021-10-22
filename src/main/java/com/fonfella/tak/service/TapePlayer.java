package com.fonfella.tak.service;

import com.fonfella.tak.util.ProcessUtils;
import com.fonfella.tak.model.ProcessOptions;
import com.fonfella.tak.model.Tape;
import com.vinz.tape.factory.EventFactory;
import com.vinz.tape.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.Thread.sleep;
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

        options.setTimeout(5000);

        options.setStdin(events.stream().peek(event -> {

            long delay = event.getDelay();

            if (delay > TapeDeck.EVENT_TIME_MIN_DELTA_millis) {

                try { sleep(delay); } catch (InterruptedException ignored) { }
            }

        }).map(event -> eventFactory.explode(event)));

        String[] shell = {"shell"};

        if (!isEmpty(did)) {

            shell = processUtils.prepender(shell, TapeDeck.DEVICE_OPTION, did);
        }

        adbExecutor.adb(options, shell);

        Tape.TapeBuilder builder = Tape.builder();
        builder.did(did);

        return builder.build();
    }
}
