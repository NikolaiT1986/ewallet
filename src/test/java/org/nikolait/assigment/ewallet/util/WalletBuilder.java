package org.nikolait.assigment.ewallet.util;

import lombok.experimental.UtilityClass;
import org.nikolait.assigment.ewallet.entity.Wallet;

import java.time.Instant;

@UtilityClass
public class WalletBuilder {

    public static Wallet newWallet(long balance) {
        return Wallet.builder()
                .balance(balance)
                .balanceUpdatedAt(Instant.now())
                .build();
    }
}
