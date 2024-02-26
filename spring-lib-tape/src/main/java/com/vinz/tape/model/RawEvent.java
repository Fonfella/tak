package com.fonfella.tape.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RawEvent {

    long timestamp;

    String line;
}
