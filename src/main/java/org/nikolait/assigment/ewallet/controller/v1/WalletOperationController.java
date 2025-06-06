package org.nikolait.assigment.ewallet.controller.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nikolait.assigment.ewallet.dto.BalanceUpdateRequest;
import org.nikolait.assigment.ewallet.service.WalletOperationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallet")
public class WalletOperationController {

    private final WalletOperationService walletOperationService;

    @PostMapping
    public ResponseEntity<Void> updateBalance(@Valid @RequestBody BalanceUpdateRequest request) {
        walletOperationService.updateBalance(
                request.getWalletId(),
                request.getOperationType(),
                request.getAmount().longValue()
        );
        return ResponseEntity.ok().build();
    }

}
