package org.nikolait.assigment.ewallet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nikolait.assigment.ewallet.BaseIntegrationTest;
import org.nikolait.assigment.ewallet.model.OperationType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WalletOperationConcurrencyTest extends BaseIntegrationTest {

    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int ITERATIONS = 10;

    private static long initialBalance = 0L;
    private static ExecutorService executor;

    @Autowired
    private WalletOperationService walletOperationService;

    @BeforeEach
    void setUp() {
        initialBalance = walletJpaRepository.findById(wallet2Id).orElseThrow().getBalance();
        executor = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    @Test
    void testConcurrentWalletOperations() throws Exception {
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            tasks.add(this::updateBalanceRandomOrderTask);
        }

        for (Future<Void> future : executor.invokeAll(tasks)) {
            future.get();
        }

        executor.shutdown();
        walletOperationService.flushToDatabase();

        long finalBalance = walletJpaRepository.findById(wallet2Id).orElseThrow().getBalance();
        assertEquals(initialBalance, finalBalance);
    }

    private Void updateBalanceRandomOrderTask() {
        for (int i = 0; i < ITERATIONS; i++) {
            if (current().nextBoolean()) {
                walletOperationService.updateBalance(wallet2Id, OperationType.DEPOSIT, 1);
                walletOperationService.updateBalance(wallet2Id, OperationType.WITHDRAW, 1);
            } else {
                walletOperationService.updateBalance(wallet2Id, OperationType.WITHDRAW, 1);
                walletOperationService.updateBalance(wallet2Id, OperationType.DEPOSIT, 1);
            }
        }
        return null;
    }
}
