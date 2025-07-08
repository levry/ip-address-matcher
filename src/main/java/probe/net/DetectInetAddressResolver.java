package probe.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.spi.InetAddressResolver;
import java.util.stream.Stream;

/**
 * @author levry
 */
public class DetectInetAddressResolver implements InetAddressResolver {
    @Override
    public Stream<InetAddress> lookupByName(String host, LookupPolicy lookupPolicy) throws UnknownHostException {
        throw new DetectResolvingHostException(host);
    }

    @Override
    public String lookupByAddress(byte[] addr) {
        return "probe.host";
    }
}
