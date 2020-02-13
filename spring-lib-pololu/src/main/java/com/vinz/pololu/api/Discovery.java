package com.vinz.pololu.api;

public interface Discovery
{
    ServoController discover();

    void clear();
}
