package org.nikolait.assigment.ewallet.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.nikolait.assigment.ewallet.BaseIntegrationTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class WalletControllerTest extends BaseIntegrationTest {

    @Value("${pagination.max-page-size}")
    private int maxPageSize;

    @Test
    void createWallet_shouldCreateAndReturnWalletData() throws Exception {
        String responseBody = mockMvc.perform(post("/api/v1/wallets"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        UUID walletId = UUID.fromString(JsonPath.read(responseBody, "$.id"));

        mockMvc.perform(get("/api/v1/wallets/{id}", walletId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(0))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.balanceUpdatedAt").isNotEmpty());
    }

    @Test
    void getWallet_shouldReturnWalletData() throws Exception {
        mockMvc.perform(get("/api/v1/wallets/{id}", wallet1Id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(4)))
                .andExpect(jsonPath("$.id").value(wallet1Id.toString()))
                .andExpect(jsonPath("$.balance").value(0))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.balanceUpdatedAt").isNotEmpty());
    }

    @Test
    void getWallet_shouldReturnNotFound_whenWalletNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/wallets/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getWallet_shouldReturnBadRequest_whenInvalidUUID() throws Exception {
        mockMvc.perform(get("/api/v1/wallets/invalid-uuid")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllWallets_shouldReturnPageOfWallets() throws Exception {
        mockMvc.perform(get("/api/v1/wallets")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void getAllWallets_shouldReturnBadRequest_whenPageSizeExceedsMax() throws Exception {
        mockMvc.perform(get("/api/v1/wallets")
                        .param("page", "0")
                        .param("size", String.valueOf(maxPageSize + 1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllWallets_shouldReturnBadRequest_whenInvalidSortParameter() throws Exception {
        mockMvc.perform(get("/api/v1/wallets")
                        .param("sort", "invalid-param,asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
