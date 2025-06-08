package org.nikolait.assigment.ewallet.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class WalletJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public Integer deposit(UUID id, long amount) {
        return jdbcTemplate.update(
                """
                        UPDATE wallets
                        SET balance = balance + ?, balance_updated_at = now()
                        WHERE id = ?
                        """,
                amount, id
        );
    }

    public Integer withdraw(UUID id, long amount) {
        return jdbcTemplate.queryForObject(
                """
                        WITH updated AS (
                            UPDATE wallets
                            SET balance = balance - ?, balance_updated_at = now()
                            WHERE id = ? AND balance - ? >= 0
                            RETURNING id
                        )
                        SELECT CASE
                            WHEN EXISTS (SELECT 1 FROM updated) THEN 1
                            ELSE (
                                SELECT CASE
                                    WHEN EXISTS (SELECT 1 FROM wallets WHERE id = ?) THEN -1
                                    ELSE 0
                                END
                            )
                        END;
                        """,
                Integer.class,
                amount, id, amount, id
        );
    }

}
