package probe.net;

import java.net.UnknownHostException;

/**
 * @author levry
 */
public class DetectResolvingHostException extends UnknownHostException {
    public DetectResolvingHostException(String host) {
        super("Host resolving detected: " + host);
    }
}
