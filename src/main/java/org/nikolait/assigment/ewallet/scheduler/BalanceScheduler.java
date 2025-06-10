package org.nikolait.assigment.ewallet.scheduler;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.nikolait.assigment.ewallet.service.WalletOperationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceScheduler {

    private final WalletOperationService walletOperationService;

    @Scheduled(
            initialDelayString = "${balance.update.initial-delay}",
            fixedRateString = "${balance.update.fixed-rate}"
    )
    @SchedulerLock(
            name = "flushToDatabaseLock",
            lockAtLeastFor = "${balance.update.fixed-rate}",
            lockAtMostFor = "${balance.update.fixed-rate}"
    )
    public void flushToDatabase() {
        walletOperationService.flushToDatabase();
    }

}
