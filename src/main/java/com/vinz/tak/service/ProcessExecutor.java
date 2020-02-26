package com.vinz.tak.service;

import com.vinz.tak.model.ExecResult;
import com.vinz.tak.model.ExecResult.ExecResultBuilder;
import com.vinz.tak.model.ProcessOptions;
import com.vinz.tak.util.ProcessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class ProcessExecutor extends AbstractService {

    @Autowired
    private ProcessUtils processUtils;

    @Value("${events.waitfor:10000}")
    public long defaultWaitFor;

    @Value("${events.waitfor.period:100}")
    public long waiterPeriodMillis;

    private AtomicLong lastLineTime = new AtomicLong(0);

    private long waitFor;

    private class WaiterTimerTak extends TimerTask {

        private final Process runningProcess;

        public WaiterTimerTak(Process process) {

            runningProcess = process;
        }

        @Override
        public void run() {

            long delay = lastLineTime.addAndGet(waiterPeriodMillis);

            if (delay > waitFor) {

                runningProcess.destroy();
            }
        }
    }

    public ExecResult shellExec(ProcessOptions options, String... cli) {

        String[] shcli = processUtils.shellPrepender(cli);

        return exec(options, shcli);
    }

    public ExecResult exec(ProcessOptions options, String... cli) {

        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command(cli);

        return runProcess(options, processBuilder);
    }

    private ExecResult runProcess(ProcessOptions options, ProcessBuilder processBuilder) {

        ExecResultBuilder resultBuilder = ExecResult.builder();
        resultBuilder.status(-255);

        Timer timer = new Timer();

        try {

            Process process = processBuilder.start();

            if (options.getTimeout() > 0) {

                waitFor = options.getTimeout();
                timer.schedule(new WaiterTimerTak(process), 0, waiterPeriodMillis);
            }

            StreamGobbler streamGobbler = new StreamGobbler(process, resultBuilder, options.getFilter());
            Executors.newSingleThreadExecutor().submit(streamGobbler);

            resultBuilder.status(process.waitFor());

        } catch (Exception e) {

            resultBuilder.message(e.getMessage());

            log.error("Error during execution: " + e.toString());

        } finally {

            timer.cancel();
        }

        return resultBuilder.build();
    }

    private class StreamGobbler implements Runnable {

        private final Process pProcess;
        private final ExecResultBuilder pResultBuilder;
        private final Predicate<String> pFilter;

        public StreamGobbler(Process process, ExecResultBuilder resultBuilder, Predicate<String> filter) {

            pProcess = process;
            pResultBuilder = resultBuilder;
            pFilter = filter;
        }

        @Override
        public void run() {

            Stream<String> lines = new BufferedReader(new InputStreamReader(pProcess.getInputStream())).lines();

            if (pFilter != null) {
                lines = lines.filter(pFilter);
            }

            lines.forEach(line -> {
                lastLineTime.set(0);
                pResultBuilder.line(line);
            });
        }
    }
}
