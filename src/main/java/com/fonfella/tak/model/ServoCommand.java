package com.fonfella.tak.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ServoCommand
{
    String servo;
    float position;
    short speed;
    short acceleration;
    long delay;
}
