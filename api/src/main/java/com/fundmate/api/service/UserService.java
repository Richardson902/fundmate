package com.fundmate.api.service;

import com.fundmate.api.dto.request.UserRequest;
import com.fundmate.api.dto.response.UserResponse;

public interface UserService {
    public UserResponse registerUser(UserRequest userRequest);
}
