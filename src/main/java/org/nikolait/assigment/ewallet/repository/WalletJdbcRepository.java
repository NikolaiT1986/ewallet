package org.nikolait.assigment.ewallet.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class WalletJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public Integer updateBalance(UUID id, long amount) {
        return jdbcTemplate.queryForObject(
                """
                        WITH updated AS (
                            UPDATE wallets
                            SET balance = balance + ?, balance_updated_at = now()
                            WHERE id = ? AND balance + ? >= 0
                            RETURNING id
                        )
                        SELECT
                            CASE
                                WHEN NOT EXISTS (SELECT 1 FROM updated) THEN (
                                    SELECT CASE
                                        WHEN ? < 0 THEN
                                            CASE WHEN EXISTS (
                                                SELECT 1 FROM wallets WHERE id = ?
                                            ) THEN 2 ELSE 0 END
                                        ELSE 0
                                    END
                                )
                                ELSE 1
                            END AS result;
                        """,
                Integer.class,
                amount, id, amount, amount, id
        );
    }


}
