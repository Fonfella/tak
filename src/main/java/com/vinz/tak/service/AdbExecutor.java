package com.vinz.tak.service;

import com.vinz.tak.model.ExecResult;
import com.vinz.tak.model.StartRecord;
import com.vinz.tak.util.ProcessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class AdbExecutor extends AbstractService {

    @Value("${adb.path:c:\\Users\\TarriconeV\\platform-tools\\adb.exe}")
    public String adb;

    @Autowired
    private ProcessUtils processUtils;

    @Autowired
    private ProcessExecutor processExecutor;

    @Value("${events.waitfor:30000")
    public long defaultWaitFor;

    public ExecResult getEvents(StartRecord startRecord) {

        String did = startRecord.getDid();

        long waitfor = startRecord.getWait();

        if(waitfor == 0) {

            waitfor = defaultWaitFor;
        }

        if (!isEmpty(did)) {

            return adb(waitfor,s -> s.startsWith("/dev/input/event"), "-s", did, "shell", "getevent");

        } else {

            return adb(waitfor, s -> s.startsWith("/dev/input/event"), "shell", "getevent");
        }
    }

    public ExecResult adb(long waitfor, Predicate<String> filter, String... cli) {

        String[] newcli = processUtils.prepender(cli, adb);

        return processExecutor.exec(waitfor, filter, newcli);
    }
}
