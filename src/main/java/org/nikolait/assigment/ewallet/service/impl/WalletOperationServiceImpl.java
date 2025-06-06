package org.nikolait.assigment.ewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.nikolait.assigment.ewallet.exception.InsufficientFundsException;
import org.nikolait.assigment.ewallet.exception.WalletNotFoundException;
import org.nikolait.assigment.ewallet.limiter.WalletRateLimiter;
import org.nikolait.assigment.ewallet.model.OperationType;
import org.nikolait.assigment.ewallet.repository.WalletJdbcRepository;
import org.nikolait.assigment.ewallet.service.WalletOperationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletOperationServiceImpl implements WalletOperationService {

    private final WalletJdbcRepository walletJdbcRepository;
    private final WalletRateLimiter rateLimiter;

    @Override
    @Transactional
    public void updateBalance(UUID id, OperationType type, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive: " + amount);
        }

        long balance = getBalanceWithLockById(id);
        rateLimiter.checkRateLimitOrThrow(id);

        long updatedBalance = switch (type) {
            case DEPOSIT -> balance + amount;
            case WITHDRAW -> balance - amount;
        };

        if (updatedBalance < 0) {
            throw new InsufficientFundsException(id, balance, amount);
        }

        walletJdbcRepository.updateBalance(id, updatedBalance);

    }

    private Long getBalanceWithLockById(UUID id) {
        return walletJdbcRepository.getBalanceByIdWithLock(id).orElseThrow(
                () -> new WalletNotFoundException(id)
        );
    }

}
