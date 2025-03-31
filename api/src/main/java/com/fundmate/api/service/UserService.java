package com.fundmate.api.service;

import com.fundmate.api.dto.request.AuthRequest;
import com.fundmate.api.dto.request.UserRequest;
import com.fundmate.api.dto.response.AuthResponse;
import com.fundmate.api.dto.response.UserResponse;

public interface UserService {
    public UserResponse registerUser(UserRequest userRequest);
    public AuthResponse authenticate(AuthRequest request);
}
