package com.vinz.tak.autoconfiguration;

import api.Discovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PololuConfiguration
{

    @Autowired
    private Discovery discovery;

    @Bean
    public Discovery getDiscovery()
    {

        return discovery;
    }
}
