package org.nikolait.assigment.ewallet.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class WalletJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public Optional<Long> getBalanceByIdWithLock(UUID id) {
        List<Long> result = jdbcTemplate.query(
                "SELECT balance FROM wallets WHERE id = ? FOR UPDATE",
                ps -> ps.setObject(1, id),
                (rs, rowNum) -> rs.getLong("balance")
        );
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public int updateBalance(UUID id, long newBalance) {
        return jdbcTemplate.update(
                "UPDATE wallets SET balance = ?, balance_updated_at = now() WHERE id = ?",
                ps -> {
                    ps.setLong(1, newBalance);
                    ps.setObject(2, id);
                }
        );
    }

}
