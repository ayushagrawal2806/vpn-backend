package com.vpn.vpn_backend.dto.Server;

import com.vpn.vpn_backend.enums.ServerStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ServerNodeDto {
    private UUID id;
    private String name;
    private String country;
    private String city;
    private String ipAddress;
    private Integer port;
    private Integer maxPeers;
    private Integer currentPeers;
    private Integer loadPercentage;
    private ServerStatus status;
    private String flagEmoji;
    private boolean available;
}