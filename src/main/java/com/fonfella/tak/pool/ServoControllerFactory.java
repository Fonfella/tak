package com.fonfella.tak.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vinz.pololu.api.Discovery;
import com.vinz.pololu.api.ServoController;


@Component
public class ServoControllerFactory extends BasePooledObjectFactory<ServoController>
{
    @Autowired
    private Discovery discovery;

    @Override
    public ServoController create()
    {
        return discovery.discover();
    }

    @Override
    public PooledObject<ServoController> wrap(ServoController servo)
    {
        return new DefaultPooledObject<>(servo);
    }

    @Override
    public boolean validateObject(PooledObject<ServoController> pooled)
    {

        boolean tested = pooled.getObject().test();

        if (!tested)
        {
            discovery.clear();
        }

        return tested;
    }
}
