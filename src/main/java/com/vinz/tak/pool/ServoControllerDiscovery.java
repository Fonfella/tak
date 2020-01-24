package com.vinz.tak.pool;

import api.ServoController;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ServoControllerDiscovery
{
    @Autowired
    private ServoControllerFactory servoControllerFactory;

    private ServoControllerPool pool;

    @PostConstruct
    public void init()
    {
        pool = new ServoControllerPool(servoControllerFactory, getConfig());
    }

    public ServoController getServoController()
    {

        ServoController servoController = null;

        while (servoController == null) {

            try {

                servoController = pool.borrowObject();

            } catch (Exception e) {

                pool.clear();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return servoController;
    }

    public void returnServoController(ServoController servoController)
    {
        pool.returnObject(servoController);
    }

    private GenericObjectPoolConfig<ServoController> getConfig()
    {

        GenericObjectPoolConfig<ServoController> config = new GenericObjectPoolConfig<>();

        config.setMaxIdle(1);
        config.setMaxTotal(1);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);

        return config;
    }

    private static class ServoControllerPool extends GenericObjectPool<ServoController>
    {
        public ServoControllerPool(ServoControllerFactory factory, GenericObjectPoolConfig<ServoController> config)
        {
            super(factory, config);
        }
    }
}
