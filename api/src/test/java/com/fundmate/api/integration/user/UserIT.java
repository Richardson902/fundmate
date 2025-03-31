package com.fundmate.api.integration.user;

import com.fundmate.api.dto.request.UserRequest;
import com.fundmate.api.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserIT extends BaseIT {

    @Test
    void registerUser_ShouldCreateAndReturnUser() throws Exception {
        UserRequest request = new UserRequest();
        request.setEmail("test@test.com");
        request.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    void authenticate_WithValidCredentials_ShouldReturnToken() throws Exception {
        // First register a user
        UserRequest registerRequest = new UserRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        // Then try to authenticate
        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void authenticate_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        UserRequest request = new UserRequest();
        request.setEmail("wrong@example.com");
        request.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
