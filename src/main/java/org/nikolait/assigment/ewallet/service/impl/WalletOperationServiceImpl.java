package org.nikolait.assigment.ewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.nikolait.assigment.ewallet.exception.InsufficientFundsException;
import org.nikolait.assigment.ewallet.exception.WalletNotFoundException;
import org.nikolait.assigment.ewallet.model.OperationType;
import org.nikolait.assigment.ewallet.repository.WalletJdbcRepository;
import org.nikolait.assigment.ewallet.service.WalletOperationService;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.nikolait.assigment.ewallet.model.OperationType.DEPOSIT;

@Service
@RequiredArgsConstructor
public class WalletOperationServiceImpl implements WalletOperationService {

    private final WalletJdbcRepository walletJdbcRepository;

    @Override
    public void updateBalance(UUID id, OperationType type, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive: " + amount);
        }

        switch (walletJdbcRepository.updateBalance(id, type == DEPOSIT ? amount : -amount)) {
            case 0 -> throw new WalletNotFoundException(id);
            case 2 -> throw new InsufficientFundsException(id);
        }
    }

}
