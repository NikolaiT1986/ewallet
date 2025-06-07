package org.nikolait.assigment.ewallet.controller;

import org.junit.jupiter.api.Test;
import org.nikolait.assigment.ewallet.BaseIntegrationTest;
import org.nikolait.assigment.ewallet.exception.PageSizeLimitException;
import org.nikolait.assigment.ewallet.util.TestUriUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class WalletControllerTest extends BaseIntegrationTest {

    @Value("${pagination.max-page-size}")
    private int maxPageSize;

    @Test
    void createWallet_shouldCreateAndReturnWalletData() throws Exception {
        String location = mockMvc.perform(post("/api/v1/wallets"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        assertNotNull(location);
        assertTrue(TestUriUtils.pathFromLocationStartsWith(location, "/api/v1/wallets/"));
        String idPart = TestUriUtils.extractIdFromLocation(location);

        mockMvc.perform(get(location).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(idPart))
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

}
