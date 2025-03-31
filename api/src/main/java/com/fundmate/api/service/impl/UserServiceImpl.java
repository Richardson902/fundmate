package com.fundmate.api.service.impl;

import com.fundmate.api.dto.request.AuthRequest;
import com.fundmate.api.dto.request.UserRequest;
import com.fundmate.api.dto.response.AuthResponse;
import com.fundmate.api.dto.response.UserResponse;
import com.fundmate.api.mapper.UserMapper;
import com.fundmate.api.model.User;
import com.fundmate.api.repository.UserRepository;
import com.fundmate.api.security.JwtService;
import com.fundmate.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        var token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
