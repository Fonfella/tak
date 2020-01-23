package com.vinz.tak.autoconfiguration;

import autoconfiguration.DiscoveryConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DiscoveryConfiguration.class)
public class PololuConfiguration
{
}
