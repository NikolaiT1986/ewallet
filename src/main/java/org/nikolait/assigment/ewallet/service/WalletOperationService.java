package org.nikolait.assigment.ewallet.service;

import org.nikolait.assigment.ewallet.model.OperationType;

import java.util.UUID;

public interface WalletOperationService {
    void updateBalance(UUID id, OperationType type, long amount);
}
