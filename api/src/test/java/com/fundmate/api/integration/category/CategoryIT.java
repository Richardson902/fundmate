package com.fundmate.api.integration.category;

import com.fundmate.api.dto.request.CategoryRequest;
import com.fundmate.api.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryIT extends BaseIT {

    @Test
    void createCategory_ShouldCreateAndReturnCategory() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setCategoryName("Groceries");
        request.setIcon("grocery-icon");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryName").value("Groceries"))
                .andExpect(jsonPath("$.icon").value("grocery-icon"));
    }

    @Test
    void getAllCategories_ShouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}