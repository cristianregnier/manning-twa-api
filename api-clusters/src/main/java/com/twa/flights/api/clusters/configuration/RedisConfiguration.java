package com.twa.flights.api.clusters.configuration;

import com.twa.flights.api.clusters.configuration.settings.RedisSettings;
import com.twa.flights.api.clusters.dto.CityDTO;
import com.twa.flights.api.clusters.dto.ClusterSearchDTO;
import com.twa.flights.api.clusters.serializer.CitySerializer;
import com.twa.flights.api.clusters.serializer.ClusterSearchSerializer;
import com.twa.flights.api.clusters.serializer.StringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableConfigurationProperties(RedisSettings.class)
public class RedisConfiguration {

    private RedisSettings redis;

    public RedisConfiguration(RedisSettings redis) {
        this.redis = redis;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        var redisStandaloneConfiguration = new RedisStandaloneConfiguration(redis.getHost(), redis.getPort());

        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, CityDTO> redisTemplate() {
        var redisTemplate = new RedisTemplate<String, CityDTO>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringSerializer());
        redisTemplate.setValueSerializer(new CitySerializer());

        return redisTemplate;
    }

    @Bean(name = "clusterSearchDTORedisTemplate")
    public RedisTemplate<String, ClusterSearchDTO> clusterSearchDTORedisTemplate() {
        var redisTemplate = new RedisTemplate<String, ClusterSearchDTO>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringSerializer());
        redisTemplate.setValueSerializer(new ClusterSearchSerializer());

        return redisTemplate;
    }

}