package com.vinz.tak.pool;

import api.Discovery;
import api.ServoController;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServoControllerFactory extends BasePooledObjectFactory<ServoController>
{

    @Autowired
    private Discovery discovery;

    @Override
    public ServoController create() throws Exception
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
        return pooled.getObject().test();
    }
}
