package org.nikolait.assigment.ewallet.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.data.redis.redisson.tuning")
public class RedissonTuningProperties {
    private int minIdle;
    private int poolSize;
    private int threads;
    private int nettyThreads;
}
