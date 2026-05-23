package com.vpn.vpn_backend.dto.Auth;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({
        "user",
        "accessToken",
        "refreshToken"
})
public class AuthResponseDto {

    private UserResponseDto user;
    private String accessToken;
    private String refreshToken;
}
