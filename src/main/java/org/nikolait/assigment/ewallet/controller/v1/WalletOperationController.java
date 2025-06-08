package org.nikolait.assigment.ewallet.controller.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nikolait.assigment.ewallet.dto.BalanceUpdateRequest;
import org.nikolait.assigment.ewallet.limiter.WalletRateLimiter;
import org.nikolait.assigment.ewallet.service.WalletOperationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallet")
public class WalletOperationController {

    private final WalletRateLimiter rateLimiter;
    private final WalletOperationService operationService;

    @PostMapping
    public void updateBalance(@Valid @RequestBody BalanceUpdateRequest request) {
        rateLimiter.checkRateLimitOrThrow(request.getWalletId());
        operationService.updateBalance(
                request.getWalletId(),
                request.getOperationType(),
                request.getAmount().longValue()
        );
    }

}
