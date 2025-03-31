package com.fundmate.api.integration.account;

import com.fundmate.api.dto.request.AccountRequest;
import com.fundmate.api.dto.response.AccountResponse;
import com.fundmate.api.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountIT extends BaseIT {

    @Test
    void createAccount_ShouldCreateAndReturnAccount() throws Exception {
        AccountRequest request = new AccountRequest();
        request.setAccountName("Test Account");
        request.setBalance(1000.0);

        mockMvc.perform(addAuth(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountName").value("Test Account"))
                .andExpect(jsonPath("$.balance").value(1000.0));
    }

    @Test
    void updateAccount_ShouldUpdateAndReturnAccount() throws Exception {
        // Create initial account
        AccountRequest createRequest = new AccountRequest();
        createRequest.setAccountName("Initial Account");
        createRequest.setBalance(1000.0);

        MvcResult accountResult = mockMvc.perform(addAuth(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest))))
                .andExpect(status().isCreated())
                .andReturn();

        Long accountId = objectMapper.readValue(
                accountResult.getResponse().getContentAsString(),
                AccountResponse.class
        ).getId();

        // Update it
        AccountRequest updateRequest = new AccountRequest();
        updateRequest.setAccountName("Updated Account");
        updateRequest.setBalance(2000.0);

        mockMvc.perform(addAuth(put("/api/accounts/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountName").value("Updated Account"))
                .andExpect(jsonPath("$.balance").value(2000.0));
    }

    @Test
    void getAccounts_ShouldReturnUserAccounts() throws Exception {
        // Create an account first
        AccountRequest request = new AccountRequest();
        request.setAccountName("Test Account");
        request.setBalance(1000.0);

        mockMvc.perform(addAuth(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))))
                .andExpect(status().isCreated());

        // Get accounts
        mockMvc.perform(addAuth(get("/api/accounts")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountName").value("Test Account"))
                .andExpect(jsonPath("$[0].balance").value(1000.0));
    }
}
