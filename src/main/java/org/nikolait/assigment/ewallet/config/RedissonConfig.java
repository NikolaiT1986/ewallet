package org.nikolait.assigment.ewallet.config;

import lombok.RequiredArgsConstructor;
import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedissonConfig implements RedissonAutoConfigurationCustomizer {

    private final RedissonTuningProperties tuningProperties;

    @Override
    public void customize(Config config) {
        config.setThreads(tuningProperties.getThreads());
        config.setNettyThreads(tuningProperties.getNettyThreads());
        config.useSingleServer()
                .setConnectionMinimumIdleSize(tuningProperties.getMinIdle())
                .setConnectionPoolSize(tuningProperties.getPoolSize());
    }

}
