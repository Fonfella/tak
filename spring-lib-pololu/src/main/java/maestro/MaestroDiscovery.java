package maestro;

import api.Discovery;

import javax.annotation.PostConstruct;

public class MaestroDiscovery implements Discovery
{

    @PostConstruct
    public void init()
    {

        //new PololuMaestroServoController();

    }

    @Override
    public void discover()
    {

    }
}
