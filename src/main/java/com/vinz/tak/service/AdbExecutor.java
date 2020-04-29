package com.vinz.tak.service;

import com.vinz.tak.model.ExecResult;
import com.vinz.tak.model.ProcessOptions;
import com.vinz.tak.util.ProcessUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class AdbExecutor extends AbstractService {

    @Value("${adb.path:C:\\Users\\donzella\\AppData\\Local\\Android\\Sdk\\platform-tools\\adb.exe}")
    public String adb;

    @Autowired
    private ProcessUtils processUtils;

    @Autowired
    private ProcessExecutor processExecutor;

    public ExecResult adb(ProcessOptions options, String... cli) {

        String[] newcli = processUtils.prepender(cli, adb);

        return processExecutor.exec(options, newcli);
    }
}
