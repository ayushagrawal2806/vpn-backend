package com.vpn.vpn_backend.service;


import com.vpn.vpn_backend.dto.Server.ServerNodeDto;
import com.vpn.vpn_backend.entity.ServerNode;
import com.vpn.vpn_backend.enums.ServerStatus;
import com.vpn.vpn_backend.exceptions.ResourceNotFoundException;
import com.vpn.vpn_backend.repository.ServerNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServerNodeService {

    private final ServerNodeRepository serverNodeRepository;

    public List<ServerNodeDto> getAllActiveServers() {
        return serverNodeRepository
                .findByStatusOrderByCurrentPeersAsc(ServerStatus.ACTIVE)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<ServerNodeDto> getAllServers() {
        return serverNodeRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<ServerNodeDto> getServersByCountry(String country) {
        return serverNodeRepository.findByCountry(country)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public ServerNode getBestAvailableServer() {
        return serverNodeRepository
                .findByStatusOrderByCurrentPeersAsc(ServerStatus.ACTIVE)
                .stream()
                .filter(ServerNode::isAvailable)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No available servers right now"));
    }

    public ServerNodeDto addServer(ServerNodeDto dto) {
        ServerNode node = ServerNode.builder()
                .name(dto.getName())
                .country(dto.getCountry())
                .city(dto.getCity())
                .ipAddress(dto.getIpAddress())
                .port(dto.getPort())
                .publicKey("")
                .maxPeers(dto.getMaxPeers())
                .flagEmoji(dto.getFlagEmoji())
                .currentPeers(0)
                .status(ServerStatus.ACTIVE)
                .build();
        return toDto(serverNodeRepository.save(node));
    }

    public ServerNodeDto updateStatus(UUID id, ServerStatus status) {
        ServerNode server = serverNodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Server not found"));
        server.setStatus(status);
        return toDto(serverNodeRepository.save(server));
    }

    private ServerNodeDto toDto(ServerNode server) {
        return ServerNodeDto.builder()
                .id(server.getId())
                .name(server.getName())
                .country(server.getCountry())
                .city(server.getCity())
                .ipAddress(server.getIpAddress())
                .port(server.getPort())
                .maxPeers(server.getMaxPeers())
                .currentPeers(server.getCurrentPeers())
                .loadPercentage(server.getLoadPercentage())
                .status(server.getStatus())
                .flagEmoji(server.getFlagEmoji())
                .available(server.isAvailable())
                .build();
    }
}