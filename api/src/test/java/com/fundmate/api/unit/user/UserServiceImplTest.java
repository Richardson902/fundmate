package com.fundmate.api.unit.user;

import com.fundmate.api.dto.request.UserRequest;
import com.fundmate.api.dto.response.UserResponse;
import com.fundmate.api.mapper.UserMapper;
import com.fundmate.api.model.User;
import com.fundmate.api.repository.UserRepository;
import com.fundmate.api.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest userRequest;
    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password123");

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("test@example.com");
    }

    @Test
    void registerUser_ShouldCreateAndReturnUser() {
        when(userRepository.findByEmail(userRequest.getEmail()))
                .thenReturn(Optional.empty());
        when(userMapper.toEntity(userRequest))
                .thenReturn(user);
        when(passwordEncoder.encode(any()))
                .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        when(userMapper.toResponse(user))
                .thenReturn(userResponse);

        UserResponse result = userService.registerUser(userRequest);

        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());
        assertEquals(userResponse.getEmail(), result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_WithExistingEmail_ShouldThrowException() {
        when(userRepository.findByEmail(userRequest.getEmail()))
                .thenReturn(Optional.of(user));

        assertThrows(ResponseStatusException.class, () ->
                userService.registerUser(userRequest));

        verify(userRepository, never()).save(any());
    }
}