package com.fundmate.api.integration.budget;

import com.fundmate.api.dto.request.*;
import com.fundmate.api.dto.response.AccountResponse;
import com.fundmate.api.dto.response.CategoryResponse;
import com.fundmate.api.dto.response.UserResponse;
import com.fundmate.api.integration.BaseIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BudgetIT extends BaseIT {

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
    void createBudget_ShouldCreateAndReturnBudget() throws Exception {
        BudgetRequest request = new BudgetRequest();
        request.setAccountId(accountId);
        request.setCategoryId(categoryId);
        request.setAmount(500.0);

        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(500.0))
                .andExpect(jsonPath("$.accountId").value(accountId))
                .andExpect(jsonPath("$.categoryId").value(categoryId));
    }
}