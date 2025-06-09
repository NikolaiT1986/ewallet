package org.nikolait.assigment.ewallet.service.impl;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikolait.assigment.ewallet.exception.InsufficientFundsException;
import org.nikolait.assigment.ewallet.exception.WalletNotFoundException;
import org.nikolait.assigment.ewallet.model.OperationType;
import org.nikolait.assigment.ewallet.repository.WalletJdbcRepository;
import org.nikolait.assigment.ewallet.service.WalletOperationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletOperationServiceImpl implements WalletOperationService {

    private final WalletJdbcRepository walletJdbcRepository;
    private final Map<UUID, Long> balanceCache;

    @Override
    public void updateBalance(UUID id, OperationType type, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive: " + amount);
        }

        synchronized (balanceCache) {
            Long balance = Optional.ofNullable(balanceCache.get(id))
                    .orElseGet(() -> walletJdbcRepository.getBalance(id));
            if (balance == null) {
                throw new WalletNotFoundException(id);
            }
            long newBalance = switch (type) {
                case DEPOSIT -> balance + amount;
                case WITHDRAW -> withdraw(id, balance, amount);
            };
            balanceCache.put(id, newBalance);
        }
    }

    @Override
    @Scheduled(
            initialDelayString = "${balance.scheduler.initial-delay}",
            fixedRateString = "${balance.scheduler.fixed-rate}"
    )
    public void flushToDatabase() {
        synchronized (balanceCache) {
            balanceCache.forEach(walletJdbcRepository::updateBalance);
            balanceCache.clear();
        }
        log.info("Balances successfully flushed to the database");
    }

    private long withdraw(UUID id, long balance, long amount) {
        if (balance < amount) {
            throw new InsufficientFundsException(id);
        }
        return balance - amount;
    }

    @PreDestroy
    private void shutdown() {
        flushToDatabase();
    }

}
