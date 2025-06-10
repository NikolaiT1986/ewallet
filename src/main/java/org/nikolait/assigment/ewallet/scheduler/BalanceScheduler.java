package org.nikolait.assigment.ewallet.scheduler;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.nikolait.assigment.ewallet.service.WalletOperationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceScheduler {

    private final WalletOperationService walletOperationService;

    @Bean
    private String lockAtMostFor(@Value("${balance.update.lock.at-most-for}") String duration) {
        return duration;
    }

    @Scheduled(cron = "${balance.update.cron}")
    @SchedulerLock(
            name = "flushToDatabaseLock",
            lockAtMostFor = "#{@lockAtMostFor}"
    )
    public void flushToDatabase() {
        walletOperationService.flushToDatabase();
    }

}
