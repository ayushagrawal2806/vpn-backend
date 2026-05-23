package com.vpn.vpn_backend.controller;


import com.vpn.vpn_backend.common.ApiResponse;
import com.vpn.vpn_backend.dto.Auth.AuthResponseDto;
import com.vpn.vpn_backend.dto.Auth.LoginRequestDto;
import com.vpn.vpn_backend.dto.Auth.RefreshRequestDto;
import com.vpn.vpn_backend.dto.Auth.RegisterRequestDto;
import com.vpn.vpn_backend.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto user){
        AuthResponseDto responseDto = authService.login(user);
        ApiResponse<AuthResponseDto> apiResponse = ApiResponse.<AuthResponseDto>builder()
                .message("User logged in successfully")
                .data(responseDto)
                .build();
        return ResponseEntity.ok(apiResponse);
    }


    @PostMapping(path = "/register")
    public ResponseEntity<ApiResponse<AuthResponseDto>> register(@Valid @RequestBody RegisterRequestDto user ){
        log.info("running");
        AuthResponseDto responseDto = authService.register(user);
        ApiResponse<AuthResponseDto> apiResponse = ApiResponse.<AuthResponseDto>builder()
                .message("User registered successfully")
                .data(responseDto)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDto>>  refresh(@Valid @RequestBody RefreshRequestDto request){
        AuthResponseDto responseDto = authService.refresh(request.getRefreshToken());
        ApiResponse<AuthResponseDto> apiResponse = ApiResponse.<AuthResponseDto>builder()
                .message("Access token refreshed successfully")
                .data(responseDto)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
