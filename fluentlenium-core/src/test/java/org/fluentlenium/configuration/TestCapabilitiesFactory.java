package org.fluentlenium.configuration;

import org.openqa.selenium.Capabilities;

@FactoryName("test-capabilities-factory")
public class TestCapabilitiesFactory implements CapabilitiesFactory {
    @Override
    public Capabilities newCapabilities(final ConfigurationProperties configuration) {
        return new TestCapabilities();
    }
}
