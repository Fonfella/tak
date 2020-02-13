package com.vinz.tak.autoconfiguration;

import com.vinz.pololu.autoconfiguration.PololuConfiguration;
import com.vinz.tape.autoconfiguration.TapeConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PololuConfiguration.class, TapeConfiguration.class})
public class TakConfiguration {
}
