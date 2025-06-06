package org.nikolait.assigment.ewallet.limiter;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.nikolait.assigment.ewallet.config.RateLimiterProperties;
import org.nikolait.assigment.ewallet.exception.RateLimitExceededException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WalletRateLimiter {

    private final RateLimiterProperties properties;
    private LoadingCache<UUID, Bucket> bucketCache;

    @PostConstruct
    public void init() {
        this.bucketCache = Caffeine.newBuilder()
                .expireAfterAccess(properties.getCacheExpire())
                .maximumSize(properties.getCacheMaxSize())
                .build(this::createNewBucket);
    }

    private Bucket createNewBucket(UUID walletId) {
        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(properties.getCapacity())
                        .refillGreedy(
                                properties.getRefillTokens(),
                                properties.getRefillDuration()
                        ).build())
                .build();
    }

    public boolean tryConsume(UUID walletId) {
        Bucket bucket = bucketCache.get(walletId);
        return bucket != null && bucket.tryConsume(1);
    }

    public void checkRateLimitOrThrow(UUID walletId) {
        if (!tryConsume(walletId)) {
            throw new RateLimitExceededException(
                    walletId,
                    properties.getCapacity(),
                    properties.getRefillDuration().toSeconds()
            );
        }
    }
}
