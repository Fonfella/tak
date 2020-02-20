package com.vinz.tak.service;

import com.vinz.tak.model.ExecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class AdbExecutor extends AbstractService {

    @Value("${adb.path:c:\\Users\\TarriconeV\\platform-tools\\adb.exe}")
    public String winAdb;

    @Value("${adb.path:c:\\Users\\TarriconeV\\platform-tools\\adb.exe}")
    public String macAdb;

    @Value("${adb.path:c:\\Users\\TarriconeV\\platform-tools\\adb.exe}")
    public String linAdb;

    @Autowired
    private ProcessExecutor processExecutor;

    boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    private int counter;

    public ExecResult getEvents() {

        return adb(new Predicate<String>() {

            @Override
            public boolean test(String s) {

                counter++;
                if(counter > 50) {
                    throw new RuntimeException("ABBASTA");
                }
                return s.startsWith("/dev/input/event");
            }

        }, "shell", "getevent");
    }

    public ExecResult adb(Predicate<String> filter, String... cli) {

        String[] newcli;

        if (isWindows) {

            newcli = ProcessExecutor.prepender(cli, winAdb);

        } else {

            newcli = ProcessExecutor.prepender(cli, macAdb);
        }

        return processExecutor.exec(filter, newcli);
    }
}
