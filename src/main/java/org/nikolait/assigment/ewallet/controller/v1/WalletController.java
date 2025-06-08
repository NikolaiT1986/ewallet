package org.nikolait.assigment.ewallet.controller.v1;

import lombok.RequiredArgsConstructor;
import org.nikolait.assigment.ewallet.dto.WalletIdResponse;
import org.nikolait.assigment.ewallet.dto.WalletResponse;
import org.nikolait.assigment.ewallet.entity.Wallet;
import org.nikolait.assigment.ewallet.exception.PageSizeLimitException;
import org.nikolait.assigment.ewallet.mapper.WalletMapper;
import org.nikolait.assigment.ewallet.service.WalletService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @Value("${pagination.max-page-size}")
    private int maxPageSize;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletIdResponse createWallet() {
        Wallet wallet = walletService.createWallet();
        return new WalletIdResponse(wallet.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable UUID id) {
        Wallet wallet = walletService.getWallet(id);
        return ResponseEntity.ok(walletMapper.toResponse(wallet));
    }

    @GetMapping
    public ResponseEntity<Page<WalletResponse>> getAllWallets(
            @ParameterObject @PageableDefault(
                    sort = "id", direction = Sort.Direction.ASC
            ) Pageable pageable) {
        if (pageable.getPageSize() > maxPageSize) {
            throw new PageSizeLimitException(maxPageSize);
        }
        Page<Wallet> wallets = walletService.getAllWallets(pageable);
        return ResponseEntity.ok(wallets.map(walletMapper::toResponse));
    }

}
