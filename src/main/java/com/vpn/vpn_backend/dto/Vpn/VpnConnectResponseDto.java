package com.vpn.vpn_backend.dto.VPN;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class VpnConnectResponseDto {
    private UUID peerId;
    private String assignedIp;
    private String serverName;
    private String serverCountry;
    private String flagEmoji;
    private String config;   // the full WireGuard .conf string
}