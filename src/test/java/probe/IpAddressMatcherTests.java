package probe;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import probe.net.DetectResolvingHostException;

import static org.assertj.core.api.Assertions.*;

/**
 * @author levry
 */
class IpAddressMatcherTests {

    @BeforeAll
    static void beforeAll() {
        System.setProperty("jdk.net.hosts.resolver", "detect");
    }

    @ValueSource(strings = {
            "10a1.1b1",
            "www",
            "10x1x1.1",
            "10-1.1.1",
            ".io"
    })
    @ParameterizedTest
    void detectResolvingHost(String ipAddress) {
        var matcher = new IpAddressMatcher("10.33.1.0/24");

        assertThatIllegalArgumentException().isThrownBy(() -> matcher.matches(ipAddress))
                .havingCause()
                .isInstanceOf(DetectResolvingHostException.class);
    }

    @Test
    void invalidAddressIpV4ThenIllegalArgumentException() {
        assertThatException().isThrownBy(() -> new IpAddressMatcher("10x1x1x1"))
                .withMessage("ipAddress 10x1x1x1 doesn't look like an IP Address. Is it a host name?");
    }

    @Test
    void matchesWhenInvalidIpV4ThenIllegalArgumentException() {
        var matcher = new IpAddressMatcher("10.1.1.0/24");

        assertThatException().isThrownBy(() -> matcher.matches("www"))
                .withMessage("ipAddress www doesn't look like an IP Address. Is it a host name?");
    }

    @CsvSource({
            "10.1.2, 10.1.0.2, true",
            "10.1.0.2, 10.1.2, true",
            "10.1, 10.0.0.1, true",
            "10.1/16, 10.0.123.1, true",
            "10.1/16, 10.0.123.23, true",
            "10.1/16, 10.3.123.23, false",
            "10.0/24, 10.0.0.12, true",
            "10.0/24, 10.0.1.12, false",
    })
    @ParameterizedTest
    void matchesWhenIpV4(String matcherAddress, String ipAddress, boolean valid) {
        var matcher = new IpAddressMatcher(matcherAddress);

        assertThat(matcher.matches(ipAddress)).isEqualTo(valid);
    }

    @Test
    void noResolvingHost() {
        var matcher = new IpAddressMatcher("10.33.1.0/24");

        assertThat(matcher.matches("10.33.1.1")).isTrue();
        assertThat(matcher.matches("10.33.1.255")).isTrue();
        assertThat(matcher.matches("10.33.2.255")).isFalse();
    }
}
