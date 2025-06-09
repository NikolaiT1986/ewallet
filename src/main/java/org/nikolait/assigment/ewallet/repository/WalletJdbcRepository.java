package org.nikolait.assigment.ewallet.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class WalletJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public Long getBalance(UUID id) {
        return jdbcTemplate.query(
                "SELECT balance FROM wallets WHERE id = ?",
                ps -> ps.setObject(1, id),
                rs -> rs.next() ? rs.getLong("balance") : null
        );
    }

    public void updateBalance(UUID id, long newBalance) {
        jdbcTemplate.update(
                "UPDATE wallets SET balance = ?, balance_updated_at = now() WHERE id = ?",
                newBalance, id
        );
    }


}
