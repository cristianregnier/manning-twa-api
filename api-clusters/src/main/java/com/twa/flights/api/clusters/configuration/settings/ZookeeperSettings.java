package com.twa.flights.api.clusters.configuration.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "zookeeper")
@ConstructorBinding
public class ZookeeperSettings {

    private final String host;
    private final int maxRetries;
    private final int timeBetweenRetries;
    private final int connectionTimeout;

    public ZookeeperSettings(String host, int maxRetries, int timeBetweenRetries, int connectionTimeout) {
        this.host = host;
        this.maxRetries = maxRetries;
        this.timeBetweenRetries = timeBetweenRetries;
        this.connectionTimeout = connectionTimeout;
    }

    public String getHost() {
        return host;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public int getTimeBetweenRetries() {
        return timeBetweenRetries;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }
}
