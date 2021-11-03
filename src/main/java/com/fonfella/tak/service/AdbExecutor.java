package com.fonfella.tak.service;

import com.fonfella.tak.model.ExecResult;
import com.fonfella.tak.util.ProcessUtils;
import com.fonfella.tak.model.ProcessOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class AdbExecutor extends AbstractService {

    @Value("${adb.path:adb.exe}")
    public String adb;

    @Autowired
    private ProcessUtils processUtils;

    @Autowired
    private ProcessExecutor processExecutor;

    public ExecResult adb(ProcessOptions options, String... cli) {

        String[] newcli = processUtils.prepender(cli, adb);
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")==true ) {
            log.info("sono windows");

        } else {
            log.info("Non sono windows ahahha");
            adb = "/usr/local/share/android-sdk/platform-tools/adb";
        }
        return processExecutor.exec(options, newcli);
    }
}