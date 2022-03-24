package com.twa.flights.api.clusters.configuration;

import com.twa.flights.api.clusters.configuration.settings.ZookeeperSettings;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ZookeeperSettings.class)
public class ZookeeperCuratorClient {

    private final ZookeeperSettings settings;

    public ZookeeperCuratorClient(ZookeeperSettings settings) {
        this.settings = settings;
    }

    @Bean
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.builder().connectString(settings.getHost())
                .retryPolicy(new RetryNTimes(settings.getMaxRetries(), settings.getTimeBetweenRetries()))
                .connectionTimeoutMs(settings.getConnectionTimeout()).build();
    }
}
