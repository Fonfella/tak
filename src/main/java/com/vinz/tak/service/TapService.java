package com.vinz.tak.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TapService extends AbstractService {

    @Autowired
    private ProcessExecutor processExecutor;

    public void tap() {

        processExecutor.exec("cmd.exe", "/c", "dir");
    }
}
