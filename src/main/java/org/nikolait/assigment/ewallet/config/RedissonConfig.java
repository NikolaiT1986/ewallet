package org.nikolait.assigment.ewallet.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedissonConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config()
                .setThreads(redisProperties.getRedisson().getThreads())
                .setNettyThreads(redisProperties.getRedisson().getNettyThreads());
        config.useSingleServer()
                .setAddress(
                        redisProperties.isSslEnabled() ? "rediss://" : "redis://" +
                                redisProperties.getHost() + ":" + redisProperties.getPort()
                )
                .setPassword(redisProperties.getPassword())
                .setClientName(redisProperties.getClientName())
                .setTimeout(redisProperties.getTimeout())
                .setConnectTimeout(redisProperties.getConnectTimeout())
                .setConnectionMinimumIdleSize(redisProperties.getRedisson().getMinIdle())
                .setConnectionPoolSize(redisProperties.getRedisson().getPoolSize());
        return Redisson.create(config);
    }
}
