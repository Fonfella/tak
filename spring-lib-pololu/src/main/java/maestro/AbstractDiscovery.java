package maestro;

import api.Discovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDiscovery implements Discovery
{
    protected Logger log = LoggerFactory.getLogger(getClass());
}
