package com.vinz.tak.service;

import com.vinz.tak.model.ExecResult;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Component
public class ProcessExecutor extends AbstractService {

    boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

    public static String[] prepender(String[] orig, String... prefixes) {

        String[] newcli = new String[orig.length + prefixes.length];

        System.arraycopy(prefixes, 0, newcli, 0, prefixes.length);
        System.arraycopy(orig, 0, newcli, prefixes.length, orig.length);

        return newcli;
    }

    public ExecResult shellExec(Predicate<String> filter, String... cli) {

        String[] shcli;

        if (isWindows) {

            shcli = prepender(cli, "cmd.exe", "/C");

        } else {

            shcli = prepender(cli, "sh", "-c");
        }

        return exec(filter, shcli);
    }

    public ExecResult exec(Predicate<String> filter, String... cli) {

        ProcessBuilder builder = new ProcessBuilder();

        builder.command(cli);

        return runProcess(builder);
    }

    private ExecResult runProcess(ProcessBuilder processBuilder) {

        ExecResult.ExecResultBuilder resultBuilder = ExecResult.builder();

        resultBuilder.status(-255);

        try {

            Process process = processBuilder.start();

            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), resultBuilder::line);
            Executors.newSingleThreadExecutor().submit(streamGobbler);

            resultBuilder.status(process.waitFor());

        } catch (Exception e) {

            resultBuilder.message(e.getMessage());

            log.error("Error during execution: " + e.toString());
        }

        return resultBuilder.build();
    }

    private static class StreamGobbler implements Runnable {

        private InputStream pInputStream;
        private Consumer<String> pConsumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {

            pInputStream = inputStream;
            pConsumer = consumer;
        }

        @Override
        public void run() {

            new BufferedReader(new InputStreamReader(pInputStream)).lines().forEach(pConsumer);
        }
    }
}
