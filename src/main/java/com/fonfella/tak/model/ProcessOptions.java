package com.fonfella.tak.model;

import lombok.Data;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Data
public class ProcessOptions {

    long timeout;
    Predicate<String> filter;
    Stream<String[]> stdin;
}
