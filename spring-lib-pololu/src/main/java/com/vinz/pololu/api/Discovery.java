package com.fonfella.pololu.api;

public interface Discovery
{
    ServoController discover();

    void clear();
}
