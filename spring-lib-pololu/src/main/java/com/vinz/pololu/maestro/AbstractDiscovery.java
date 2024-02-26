package com.fonfella.pololu.maestro;

import com.fonfella.pololu.api.Discovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDiscovery implements Discovery
{
    protected Logger log = LoggerFactory.getLogger(getClass());
}
