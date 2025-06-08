package org.nikolait.assigment.ewallet.controller;

import org.junit.jupiter.api.Test;
import org.nikolait.assigment.ewallet.BaseIntegrationTest;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WalletOperationControllerTest extends BaseIntegrationTest {

    @Test
    void updateBalance_shouldDepositToWallet() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "walletId": "%s",
                                  "operationType": "DEPOSIT",
                                  "amount": 1000
                                }
                                """.formatted(wallet1Id)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/wallets/{id}", wallet1Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    void updateBalance_shouldWithdrawFromWallet() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "walletId": "%s",
                                  "operationType": "WITHDRAW",
                                  "amount": 20
                                }
                                """.formatted(wallet2Id)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/wallets/{id}", wallet2Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(80));
    }

    @Test
    void updateBalance_shouldReturnBadRequest_whenAmountIsDecimal() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "walletId": "%s",
                                  "operationType": "DEPOSIT",
                                  "amount": 100.75
                                }
                                """.formatted(wallet1Id)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBalance_shouldReturnBadRequest_whenAmountHasTrailingZeros() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "walletId": "%s",
                                  "operationType": "DEPOSIT",
                                  "amount": 100.0
                                }
                                """.formatted(wallet1Id)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBalance_shouldReturnUnprocessableEntity_whenNotEnoughFunds() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "walletId": "%s",
                                  "operationType": "WITHDRAW",
                                  "amount": 100
                                }
                                """.formatted(wallet1Id)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateBalanceDeposite_shouldReturnNotFound_whenWalletNotFound() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                   "walletId": "%s",
                                    "operationType": "DEPOSIT",
                                    "amount": 1000
                                }
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBalanceWithdraw_shouldReturnNotFound_whenWalletNotFound() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                   "walletId": "%s",
                                    "operationType": "WITHDRAW",
                                    "amount": 1000
                                }
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBalance_shouldReturnBadRequest_whenInvalidUUID() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "walletId": "invalid-uuid",
                                  "operationType": "DEPOSIT",
                                   "amount": 1000
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBalance_shouldReturnBadRequest_whenWalletIdMissing() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "operationType": "DEPOSIT",
                                  "amount": 1000
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBalance_shouldReturnBadRequest_whenOperationTypeMissing() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "walletId": "%s",
                                  "amount": 1000
                                }
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBalance_shouldReturnBadRequest_whenAmountIsZero() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "walletId": "%s",
                                  "operationType": "DEPOSIT",
                                  "amount": 0
                                }
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBalance_shouldReturnBadRequest_whenAmountIsNegative() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "walletId": "%s",
                                  "operationType": "DEPOSIT",
                                  "amount": -100
                                }
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBalance_shouldReturnBadRequest_whenOperationTypeIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "walletId": "%s",
                                  "operationType": "INVALID_TYPE",
                                  "amount": 100
                                }
                                """.formatted(wallet1Id)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBalance_shouldReturnBadRequest_whenBodyIsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBalance_shouldReturnBadRequest_whenJsonIsMalformed() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "walletId": "%s",
                                  "operationType": "DEPOSIT"
                                  "amount": 100
                                }
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBalance_shouldReturnUnsupportedMediaType_whenContentTypeIsWrong() throws Exception {
        mockMvc.perform(post("/api/v1/wallet").contentType(MediaType.TEXT_PLAIN)
                        .content("""
                                {
                                  "walletId": "%s",
                                  "operationType": "DEPOSIT",
                                  "amount": 100
                                }
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void updateBalance_shouldReturnMethodNotAllowed_whenUsingDeleteMethod() throws Exception {
        mockMvc.perform(delete("/api/v1/wallet").contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "walletId": "%s",
                                  "operationType": "DEPOSIT",
                                  "amount": 100
                                }
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isMethodNotAllowed());
    }

}
