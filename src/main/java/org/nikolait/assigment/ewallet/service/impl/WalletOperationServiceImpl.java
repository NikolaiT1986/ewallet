package org.nikolait.assigment.ewallet.service.impl;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikolait.assigment.ewallet.exception.InsufficientFundsException;
import org.nikolait.assigment.ewallet.exception.WalletNotFoundException;
import org.nikolait.assigment.ewallet.model.OperationType;
import org.nikolait.assigment.ewallet.repository.WalletJdbcRepository;
import org.nikolait.assigment.ewallet.service.WalletOperationService;
import org.redisson.api.RMap;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletOperationServiceImpl implements WalletOperationService {

    private final WalletJdbcRepository walletJdbcRepository;
    private final RMap<UUID, Long> balanceCache;

    @Override
    public void updateBalance(UUID id, OperationType type, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive: " + amount);
        }

        balanceCache.compute(id, (k, balance) -> computeBalance(id, type, balance, amount));
    }

    @Override
    public void flushToDatabase() {
        balanceCache.forEach((id, balance) -> {
            walletJdbcRepository.updateBalance(id, balance);
            balanceCache.remove(id, balance);
        });
        log.info("Balances successfully flushed to the database");
    }

    private long computeBalance(UUID id, OperationType type, Long oldBalance, long amount) {
        Long balance = Optional.ofNullable(oldBalance).orElseGet(() -> walletJdbcRepository.getBalance(id));
        if (balance == null) {
            throw new WalletNotFoundException(id);
        }
        return switch (type) {
            case DEPOSIT -> balance + amount;
            case WITHDRAW -> withdraw(id, balance, amount);
        };
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
