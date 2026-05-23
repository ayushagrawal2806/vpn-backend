package com.vpn.vpn_backend.service;


import com.vpn.vpn_backend.Security.JWTService;
import com.vpn.vpn_backend.dto.Auth.AuthResponseDto;
import com.vpn.vpn_backend.dto.Auth.LoginRequestDto;
import com.vpn.vpn_backend.dto.Auth.RegisterRequestDto;
import com.vpn.vpn_backend.dto.Auth.UserResponseDto;
import com.vpn.vpn_backend.entity.User;
import com.vpn.vpn_backend.enums.Roles;
import com.vpn.vpn_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Roles.USER)          // always USER, never trust client
                .enabled(true)
                .build();

        userRepository.save(user);

        return buildAuthResponse(user);
    }

    public AuthResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return buildAuthResponse(user);
    }

    public AuthResponseDto refresh(String refreshToken) {
        UUID userIdFromToken = jwtService.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(userIdFromToken).orElseThrow();

        return buildAuthResponse(user);
    }

    private AuthResponseDto buildAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponseDto)
                .build();
    }
}