package org.nikolait.assigment.ewallet.repository;

import org.nikolait.assigment.ewallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletJpaRepository extends JpaRepository<Wallet, UUID> {
}
