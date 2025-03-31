package com.fundmate.api.unit.user;

import com.fundmate.api.dto.request.AuthRequest;
import com.fundmate.api.dto.request.UserRequest;
import com.fundmate.api.dto.response.AuthResponse;
import com.fundmate.api.dto.response.UserResponse;
import com.fundmate.api.mapper.UserMapper;
import com.fundmate.api.model.User;
import com.fundmate.api.repository.UserRepository;
import com.fundmate.api.security.JwtService;
import com.fundmate.api.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtService jwtService;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequest userRequest;
    private User user;
    private UserResponse userResponse;
    private AuthRequest authRequest;

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

        authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password123");
    }

    private void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    void registerUser_ShouldCreateAndReturnUser() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.registerUser(userRequest);

        assertNotNull(result);
        assertEquals(userResponse.getId(), result.getId());
        assertEquals(userResponse.getEmail(), result.getEmail());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(any(String.class));
    }

    @Test
    void registerUser_WithExistingEmail_ShouldThrowException() {
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.registerUser(userRequest));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Email already exists", exception.getReason());
        verify(userRepository, never()).save(any());
    }

    @Test
    void authenticate_ShouldReturnAuthResponse() {
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt.token.here");

        AuthResponse result = userService.authenticate(authRequest);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals("jwt.token.here", result.getToken());
        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateToken(user);
    }

    @Test
    void authenticate_WithInvalidCredentials_ShouldThrowException() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () ->
                userService.authenticate(authRequest));

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void authenticate_WithNonExistentUser_ShouldThrowException() {
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.authenticate(authRequest));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
        verify(jwtService, never()).generateToken(any());
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
}