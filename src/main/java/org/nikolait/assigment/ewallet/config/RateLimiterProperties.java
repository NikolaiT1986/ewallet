package org.nikolait.assigment.ewallet.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "rate-limiter")
public class RateLimiterProperties {
    private int capacity;
    private int refillTokens;
    private Duration refillDuration;
    private Duration cacheExpire;
    private long cacheMaxSize;
}
