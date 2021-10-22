package com.fonfella.tak.model;

import lombok.Data;

@Data
public class Command
{
    String token;
    String servo;
    long wait;
}
