package com.fundmate.api.integration.account;

import com.fundmate.api.dto.request.AccountRequest;
import com.fundmate.api.dto.request.UserRequest;
import com.fundmate.api.dto.response.AccountResponse;
import com.fundmate.api.dto.response.UserResponse;
import com.fundmate.api.integration.BaseIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountIT extends BaseIT {
    private Long userId;

    @BeforeEach
    void setUp() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        UserResponse userResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserResponse.class
        );
        userId = userResponse.getId();
    }

    @Test
    void createAccount_ShouldCreateAndReturnAccount() throws Exception {
        AccountRequest request = new AccountRequest();
        request.setAccountName("Test Account");
        request.setBalance(1000.0);

        mockMvc.perform(post("/api/accounts")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
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

        MvcResult accountResult = mockMvc.perform(post("/api/accounts")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
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

        mockMvc.perform(put("/api/accounts/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountName").value("Updated Account"))
                .andExpect(jsonPath("$.balance").value(2000.0));
    }
}
