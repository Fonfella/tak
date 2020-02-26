package com.vinz.tak.model;

import lombok.Data;

import java.util.function.Predicate;

@Data
public class ProcessOptions {

    long timeout;
    Predicate<String> filter;
}
