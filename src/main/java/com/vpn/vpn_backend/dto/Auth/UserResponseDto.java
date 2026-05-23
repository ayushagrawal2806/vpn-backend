package com.vpn.vpn_backend.dto.Auth;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.vpn.vpn_backend.enums.Roles;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "email",
        "role",
        "createdAt",
        "updatedAt"
})
public class UserResponseDto {

    private UUID id;
    private String name;
    private String email;
    private Roles role;
}
