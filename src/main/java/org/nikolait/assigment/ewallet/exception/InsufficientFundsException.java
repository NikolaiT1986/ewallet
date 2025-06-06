package org.nikolait.assigment.ewallet.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InsufficientFundsException extends RuntimeException {

    private final UUID walletId;
    private final long currentBalance;
    private final long requestedAmount;

    public InsufficientFundsException(UUID walletId, long currentBalance, long requestedAmount) {
        super("Insufficient funds in wallet %s: balance = %d, requested = %d"
                .formatted(walletId, currentBalance, requestedAmount));
        this.walletId = walletId;
        this.currentBalance = currentBalance;
        this.requestedAmount = requestedAmount;
    }

}
