package com.vpn.vpn_backend.controller;

import com.vpn.vpn_backend.common.ApiResponse;
import com.vpn.vpn_backend.dto.VPN.VpnConnectRequestDto;
import com.vpn.vpn_backend.dto.VPN.VpnConnectResponseDto;
import com.vpn.vpn_backend.entity.User;
import com.vpn.vpn_backend.service.VpnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vpn")
@RequiredArgsConstructor
public class VpnController {

    private final VpnService vpnService;

    @PostMapping("/connect")
    public ResponseEntity<ApiResponse<VpnConnectResponseDto>> connect(
            @AuthenticationPrincipal User user,
            @RequestBody(required = false) VpnConnectRequestDto request) {

        if (request == null) request = new VpnConnectRequestDto();

        return ResponseEntity.ok(
                ApiResponse.<VpnConnectResponseDto>builder()
                        .message("Connected successfully")
                        .data(vpnService.connect(user, request))
                        .build()
        );
    }

    @PostMapping("/disconnect")
    public ResponseEntity<ApiResponse<Void>> disconnect(@AuthenticationPrincipal User user) {
        vpnService.disconnect(user);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Disconnected successfully")
                        .build()
        );
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<VpnConnectResponseDto>> status(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                ApiResponse.<VpnConnectResponseDto>builder()
                        .message("Status fetched successfully")
                        .data(vpnService.getStatus(user))
                        .build()
        );
    }
}