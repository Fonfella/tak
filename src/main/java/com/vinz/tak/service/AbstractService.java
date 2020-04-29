package com.vinz.tak.service;

import com.vinz.tak.exception.HttpExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService
{
    @Autowired
    protected HttpExceptions exceptions;

    Logger log = LoggerFactory.getLogger(getClass());

    protected void waitMs(long millis)
    {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException ignored) {
        }
    }
}
