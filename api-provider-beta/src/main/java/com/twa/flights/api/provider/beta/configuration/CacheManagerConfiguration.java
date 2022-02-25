package com.twa.flights.api.provider.beta.configuration;

import com.twa.flights.api.provider.beta.configuration.settings.CacheSettings;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheSettings.class)
public class CacheManagerConfiguration {

    private final CacheSettings cacheSettings;

    public CacheManagerConfiguration(CacheSettings cacheSettings) {
        this.cacheSettings = cacheSettings;
    }

}
