package com.vinz.tak.service;

import com.vinz.tak.model.ExecResult;
import com.vinz.tak.model.ExecResult.ExecResultBuilder;
import com.vinz.tak.util.ProcessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
public class ProcessExecutor extends AbstractService {

    @Autowired
    private ProcessUtils processUtils;

    public ExecResult shellExec(long waitfor, Predicate<String> filter, String... cli) {

        String[] shcli = processUtils.shellPrepender(cli);

        return exec(waitfor, filter, shcli);
    }

    public ExecResult exec(long waitfor, Predicate<String> filter, String... cli) {

        ProcessBuilder builder = new ProcessBuilder();

        builder.command(cli);

        return runProcess(builder, filter);
    }

    private ExecResult runProcess(ProcessBuilder processBuilder, Predicate<String> filter) {

        ExecResultBuilder resultBuilder = ExecResult.builder();

        resultBuilder.status(-255);

        try {

            Process process = processBuilder.start();

            StreamGobbler streamGobbler = new StreamGobbler(process, resultBuilder, filter);

            Executors.newSingleThreadExecutor().submit(streamGobbler);

            resultBuilder.status(process.waitFor());

        } catch (Exception e) {

            resultBuilder.message(e.getMessage());

            log.error("Error during execution: " + e.toString());
        }

        return resultBuilder.build();
    }

    private static class StreamGobbler implements Runnable {

        private final Process pProcess;
        private final ExecResultBuilder pResultBuilder;
        private final Predicate<String> pFilter;
        private int counter;

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

            lines.filter(new Predicate<String>() {

                @Override
                public boolean test(String s) {

                    if (counter++ > 50) {

                        pProcess.destroy();
                    }
                    return true;
                }
            }).forEach(pResultBuilder::line);
        }
    }
}
