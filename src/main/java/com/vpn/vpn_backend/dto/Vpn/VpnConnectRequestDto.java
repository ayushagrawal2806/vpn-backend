package com.vpn.vpn_backend.dto.VPN;

import lombok.Data;

import java.util.UUID;

@Data
public class VpnConnectRequestDto {
    // optional — if null, backend picks the best server automatically
    private UUID serverId;
}