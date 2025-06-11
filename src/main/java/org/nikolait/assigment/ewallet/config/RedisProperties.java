package org.nikolait.assigment.ewallet.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {
    private String host;
    private int port;
    private String password;
    private int timeout;
    private int connectTimeout;
    private String clientName;
    private boolean sslEnabled;

    @NestedConfigurationProperty
    private RedissonProperties redisson;

    @Getter
    @Setter
    public static class RedissonProperties {
        private int minIdle;
        private int poolSize;
        private int threads;
        private int nettyThreads;
    }
}
