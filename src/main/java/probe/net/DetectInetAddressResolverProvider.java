package probe.net;

import java.net.spi.InetAddressResolver;
import java.net.spi.InetAddressResolverProvider;

/**
 * @author levry
 */
public class DetectInetAddressResolverProvider extends InetAddressResolverProvider {

    @Override
    public InetAddressResolver get(Configuration configuration) {
        return new DetectInetAddressResolver();
    }

    @Override
    public String name() {
        return "detect";
    }

}
