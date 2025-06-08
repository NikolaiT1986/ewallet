package org.nikolait.assigment.ewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.nikolait.assigment.ewallet.exception.InsufficientFundsException;
import org.nikolait.assigment.ewallet.exception.WalletNotFoundException;
import org.nikolait.assigment.ewallet.model.OperationType;
import org.nikolait.assigment.ewallet.repository.WalletJdbcRepository;
import org.nikolait.assigment.ewallet.service.WalletOperationService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletOperationServiceImpl implements WalletOperationService {

    private final WalletJdbcRepository walletJdbcRepository;

    @Override
    public void updateBalance(UUID id, OperationType type, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive: " + amount);
        }

        int result = switch (type) {
            case DEPOSIT -> walletJdbcRepository.deposit(id, amount);
            case WITHDRAW -> walletJdbcRepository.withdraw(id, amount);
        };

        switch (result) {
            case 0 -> throw new WalletNotFoundException(id);
            case -1 -> throw new InsufficientFundsException(id);
        }
    }

}
