package org.nikolait.assigment.ewallet.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InsufficientFundsException extends RuntimeException {

    private final UUID walletId;

    public InsufficientFundsException(UUID walletId) {
        super("Insufficient funds in wallet %s".formatted(walletId));
        this.walletId = walletId;
    }

}
