package com.fundmate.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fundmate.api.dto.request.UserRequest;
import com.fundmate.api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ScheduledTransactionRepository scheduledTransactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    protected String authToken;

    @BeforeEach
    void setUp() throws Exception {
        cleanDatabase();
        authToken = registerAndLogin();
    }

    void cleanDatabase() {
        scheduledTransactionRepository.deleteAll();
        transactionRepository.deleteAll();
        budgetRepository.deleteAll();
        categoryRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    protected String registerAndLogin() throws Exception {
        // Register
        UserRequest request = new UserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Login
        MvcResult result = mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("token").asText();
    }

    // Helper method for authenticated requests
    protected MockHttpServletRequestBuilder addAuth(MockHttpServletRequestBuilder request) {
        return request.header("Authorization", "Bearer " + authToken);
    }
}
