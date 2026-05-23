package com.vpn.vpn_backend.dto.Auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshRequestDto {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}