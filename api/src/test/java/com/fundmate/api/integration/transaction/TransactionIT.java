package com.fundmate.api.integration.transaction;

import com.fundmate.api.dto.request.AccountRequest;
import com.fundmate.api.dto.request.CategoryRequest;
import com.fundmate.api.dto.request.TransactionRequest;
import com.fundmate.api.dto.request.UserRequest;
import com.fundmate.api.dto.response.AccountResponse;
import com.fundmate.api.dto.response.CategoryResponse;
import com.fundmate.api.dto.response.TransactionResponse;
import com.fundmate.api.dto.response.UserResponse;
import com.fundmate.api.integration.BaseIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionIT extends BaseIT {

    private Long accountId;
    private Long categoryId;

    @BeforeEach
    void setUp() throws Exception {
        // Create user
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password123");

        MvcResult userResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        UserResponse userResponse = objectMapper.readValue(
                userResult.getResponse().getContentAsString(),
                UserResponse.class
        );

        // Create account
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountName("Test Account");
        accountRequest.setBalance(1000.0);

        MvcResult accountResult = mockMvc.perform(post("/api/accounts")
                        .param("userId", userResponse.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        AccountResponse accountResponse = objectMapper.readValue(
                accountResult.getResponse().getContentAsString(),
                AccountResponse.class
        );
        accountId = accountResponse.getId();

        // Create category
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setCategoryName("Groceries");
        categoryRequest.setIcon("grocery-icon");

        MvcResult categoryResult = mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryResponse categoryResponse = objectMapper.readValue(
                categoryResult.getResponse().getContentAsString(),
                CategoryResponse.class
        );
        categoryId = categoryResponse.getId();
    }

    @Test
    void createTransaction_ShouldCreateAndReturnTransaction() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(100.0);
        request.setAccountId(accountId);
        request.setCategoryId(categoryId);
        request.setDate(LocalDate.now());
        request.setFromName("Grocery Store");
        request.setNote("Weekly groceries");

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.account.id").value(accountId))
                .andExpect(jsonPath("$.category.id").value(categoryId))
                .andExpect(jsonPath("$.fromName").value("Grocery Store"))
                .andExpect(jsonPath("$.note").value("Weekly groceries"));
    }

    @Test
    void updateTransaction_ShouldUpdateAndReturnTransaction() throws Exception {
        // First create a transaction
        TransactionRequest createRequest = new TransactionRequest();
        createRequest.setAmount(100.0);
        createRequest.setAccountId(accountId);
        createRequest.setCategoryId(categoryId);
        createRequest.setDate(LocalDate.now());
        createRequest.setFromName("Grocery Store");
        createRequest.setNote("Weekly groceries");

        MvcResult result = mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        Long transactionId = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                TransactionResponse.class
        ).getId();

        // Then update it
        TransactionRequest updateRequest = new TransactionRequest();
        updateRequest.setAmount(150.0);
        updateRequest.setAccountId(accountId);
        updateRequest.setCategoryId(categoryId);
        updateRequest.setDate(LocalDate.now());
        updateRequest.setFromName("Updated Store");
        updateRequest.setNote("Updated note");

        mockMvc.perform(put("/api/transactions/" + transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(150.0))
                .andExpect(jsonPath("$.account.id").value(accountId))
                .andExpect(jsonPath("$.category.id").value(categoryId))
                .andExpect(jsonPath("$.fromName").value("Updated Store"))
                .andExpect(jsonPath("$.note").value("Updated note"));
    }
}