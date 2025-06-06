package org.nikolait.assigment.ewallet.dto;

import java.time.Instant;
import java.util.UUID;

public record WalletResponse(
        UUID id,
        long balance,
        Instant createdAt,
        Instant balanceUpdatedAt
) {
}
