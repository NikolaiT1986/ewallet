package org.nikolait.assigment.ewallet.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class WalletNotFoundException extends RuntimeException {

    private final UUID walletId;

    public WalletNotFoundException(UUID walletId) {
        super("Wallet not found with id: " + walletId);
        this.walletId = walletId;
    }

}
