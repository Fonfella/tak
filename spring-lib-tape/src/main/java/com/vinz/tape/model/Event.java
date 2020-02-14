package com.vinz.tape.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {

    int device;
    int command;
    int argument;
    int value;
}
