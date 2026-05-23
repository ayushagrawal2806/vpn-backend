package com.vpn.vpn_backend.controller;

import com.vpn.vpn_backend.common.ApiResponse;

import com.vpn.vpn_backend.dto.Server.ServerNodeDto;
import com.vpn.vpn_backend.enums.ServerStatus;
import com.vpn.vpn_backend.service.ServerNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerNodeController {

    private final ServerNodeService serverNodeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ServerNodeDto>>> getActiveServers() {
        return ResponseEntity.ok(
                ApiResponse.<List<ServerNodeDto>>builder()
                        .message("Active servers fetched successfully")
                        .data(serverNodeService.getAllActiveServers())
                        .build()
        );
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<ApiResponse<List<ServerNodeDto>>> getByCountry(@PathVariable String country) {
        return ResponseEntity.ok(
                ApiResponse.<List<ServerNodeDto>>builder()
                        .message("Servers fetched successfully")
                        .data(serverNodeService.getServersByCountry(country))
                        .build()
        );
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ServerNodeDto>>> getAllServers() {
        return ResponseEntity.ok(
                ApiResponse.<List<ServerNodeDto>>builder()
                        .message("All servers fetched successfully")
                        .data(serverNodeService.getAllServers())
                        .build()
        );
    }

    @PostMapping("/admin/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServerNodeDto>> addServer(@RequestBody ServerNodeDto dto) {
        return ResponseEntity.ok(
                ApiResponse.<ServerNodeDto>builder()
                        .message("Server added successfully")
                        .data(serverNodeService.addServer(dto))
                        .build()
        );
    }

    @PatchMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ServerNodeDto>> updateStatus(
            @PathVariable UUID id,
            @RequestParam ServerStatus status) {
        return ResponseEntity.ok(
                ApiResponse.<ServerNodeDto>builder()
                        .message("Server status updated successfully")
                        .data(serverNodeService.updateStatus(id, status))
                        .build()
        );
    }
}