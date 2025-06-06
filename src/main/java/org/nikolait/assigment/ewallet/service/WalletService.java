package org.nikolait.assigment.ewallet.service;

import org.nikolait.assigment.ewallet.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface WalletService {

    Wallet createWallet();

    Wallet getWallet(UUID id);

    Page<Wallet> getAllWallets(Pageable pageable);
}
