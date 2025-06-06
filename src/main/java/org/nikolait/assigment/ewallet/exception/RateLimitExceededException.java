package org.nikolait.assigment.ewallet.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RateLimitExceededException extends RuntimeException {

    private final UUID walletId;
    private final long allowedRequests;
    private final long refillPeriodSeconds;

    public RateLimitExceededException(UUID walletId, long allowedRequests, long refillPeriodSeconds) {
        super("Rate limit exceeded for wallet %s: max %d req/sec, try again in ~%d seconds"
                .formatted(walletId, allowedRequests, refillPeriodSeconds));
        this.walletId = walletId;
        this.allowedRequests = allowedRequests;
        this.refillPeriodSeconds = refillPeriodSeconds;
    }

}
