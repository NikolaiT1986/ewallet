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
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletJpaRepository walletJpaRepository;
    private final Map<UUID, Long> balanceCache;

    @Override
    public Wallet createWallet() {
        return walletJpaRepository.save(Wallet.builder()
                .balance(0L)
                .balanceUpdatedAt(Instant.now())
                .build());
    }

    @Override
    public Wallet getWallet(UUID id) {
        Wallet wallet = walletJpaRepository.findById(id).orElseThrow(() -> new WalletNotFoundException(id));
        return applyActualBalance(wallet);
    }

    @Override
    public Page<Wallet> getAllWallets(Pageable pageable) {
        return walletJpaRepository.findAll(pageable).map(this::applyActualBalance);
    }

    private Wallet applyActualBalance(Wallet wallet) {
        Long actualBalance = balanceCache.get(wallet.getId());
        if (actualBalance != null) {
            wallet.setBalance(actualBalance);
        }
        return wallet;
    }
}
