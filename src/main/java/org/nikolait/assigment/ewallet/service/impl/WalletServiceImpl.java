package org.nikolait.assigment.ewallet.service.impl;

import lombok.RequiredArgsConstructor;
import org.nikolait.assigment.ewallet.entity.Wallet;
import org.nikolait.assigment.ewallet.exception.WalletNotFoundException;
import org.nikolait.assigment.ewallet.repository.WalletJpaRepository;
import org.nikolait.assigment.ewallet.service.WalletService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletJpaRepository walletJpaRepository;

    @Override
    public Wallet createWallet() {
        return walletJpaRepository.save(Wallet.builder()
                .balance(0L)
                .balanceUpdatedAt(Instant.now())
                .build());
    }

    @Override
    public Wallet getWallet(UUID id) {
        return walletJpaRepository.findById(id).orElseThrow(() -> new WalletNotFoundException(id));
    }

    @Override
    public Page<Wallet> getAllWallets(Pageable pageable) {
        return walletJpaRepository.findAll(pageable);
    }
}
