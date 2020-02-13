package com.vinz.pololu.maestro;

import com.vinz.pololu.api.Discovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDiscovery implements Discovery
{
    protected Logger log = LoggerFactory.getLogger(getClass());
}
