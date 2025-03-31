package com.fundmate.api.integration.budget;

import com.fundmate.api.dto.request.*;
import com.fundmate.api.dto.response.AccountResponse;
import com.fundmate.api.dto.response.CategoryResponse;
import com.fundmate.api.dto.response.UserResponse;
import com.fundmate.api.integration.BaseIT;
import com.fundmate.api.model.BudgetDuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BudgetIT extends BaseIT {

    private Long accountId;
    private Long categoryId;

    @BeforeEach
    void setUp() throws Exception {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountName("Test Account");
        accountRequest.setBalance(1000.0);

        MvcResult accountResult = mockMvc.perform(addAuth(post("/api/accounts"))
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

        MvcResult categoryResult = mockMvc.perform(addAuth(post("/api/categories"))
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
        request.setDuration(BudgetDuration.ONE_MONTH);

        LocalDate today = LocalDate.now();

        mockMvc.perform(addAuth(post("/api/budgets"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(500.0))
                .andExpect(jsonPath("$.account.id").value(accountId))
                .andExpect(jsonPath("$.category.id").value(categoryId))
                .andExpect(jsonPath("$.duration").value("ONE_MONTH"))
                .andExpect(jsonPath("$.startDate").value(today.toString()))
                .andExpect(jsonPath("$.endDate").value(today.plusMonths(1).toString()));
    }
}