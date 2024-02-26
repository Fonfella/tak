package com.fonfella.pololu.api;

public interface Discovery
{
    com.fonfella.pololu.api.ServoController discover();

    void clear();
}
