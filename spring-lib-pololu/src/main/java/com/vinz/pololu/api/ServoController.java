package com.vinz.pololu.api;

public interface ServoController
{
    void raw(String servoId, float position, short speed, short acceleration);

    boolean test();
}
