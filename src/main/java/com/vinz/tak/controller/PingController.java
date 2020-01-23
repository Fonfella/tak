package com.vinz.tak.controller;

import api.Discovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController extends AbstractController
{

    @Autowired
    private Discovery discovery;

    @GetMapping("/ping")
    public void ping()
    {

        log.info("Ping Controller");

        discovery.discover();
    }
}
