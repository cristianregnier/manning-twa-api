package com.twa.flights.api.provider.beta.configuration.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "cache")
@ConstructorBinding
public class CacheSettings {

    private final String host;
    private final int port;

    public CacheSettings(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

}
