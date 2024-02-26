package com.fonfella.tape.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {

    long delay;

    int device;
    int command;
    int argument;
    long value;
}
