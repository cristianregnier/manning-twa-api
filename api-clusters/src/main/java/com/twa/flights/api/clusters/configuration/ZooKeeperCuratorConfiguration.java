package com.twa.flights.api.clusters.configuration;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
public class ZooKeeperCuratorConfiguration {

    private final CuratorFramework client;

    public ZooKeeperCuratorConfiguration(CuratorFramework client) {
        this.client = client;
        this.client.start();
    }

    public CuratorFramework getClient() {
        return client;
    }

    @PreDestroy
    public void close() {
        client.close();
    }
}
