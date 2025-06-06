package org.nikolait.assigment.ewallet.controller.v1;

import lombok.RequiredArgsConstructor;
import org.nikolait.assigment.ewallet.dto.WalletResponse;
import org.nikolait.assigment.ewallet.entity.Wallet;
import org.nikolait.assigment.ewallet.mapper.WalletMapper;
import org.nikolait.assigment.ewallet.service.WalletService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.nikolait.assigment.ewallet.util.UriUtil.buildResourceUri;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;
    private final WalletMapper walletMapper;


    @PostMapping
    public ResponseEntity<Void> createWallet() {
        Wallet wallet = walletService.createWallet();
        return ResponseEntity
                .created(buildResourceUri(wallet.getId()))
                .build();
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
        Page<Wallet> wallets = walletService.getAllWallets(pageable);
        return ResponseEntity.ok(wallets.map(walletMapper::toResponse));
    }

}
