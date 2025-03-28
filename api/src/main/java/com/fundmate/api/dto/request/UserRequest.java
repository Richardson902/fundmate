package com.fundmate.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 6, max = 50)
    private String password;
}
