package com.vpn.vpn_backend.service;

import com.vpn.vpn_backend.dto.VPN.VpnConnectRequestDto;
import com.vpn.vpn_backend.dto.VPN.VpnConnectResponseDto;
import com.vpn.vpn_backend.entity.ServerNode;
import com.vpn.vpn_backend.entity.User;
import com.vpn.vpn_backend.entity.WireguardPeer;
import com.vpn.vpn_backend.enums.PeerStatus;
import com.vpn.vpn_backend.enums.ServerStatus;
import com.vpn.vpn_backend.exceptions.ResourceNotFoundException;
import com.vpn.vpn_backend.repository.ServerNodeRepository;
import com.vpn.vpn_backend.repository.WireguardPeerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VpnService {

    private final WireguardPeerRepository wireguardPeerRepository;
    private final ServerNodeRepository serverNodeRepository;
    private final ServerNodeService serverNodeService;
    private final WireguardConfigService wireguardConfigService;

    @Transactional
    public VpnConnectResponseDto connect(User user, VpnConnectRequestDto request) {

        // 1. check if user already has an active peer
        Optional<WireguardPeer> existingPeer = wireguardPeerRepository
                .findByUserAndStatus(user, PeerStatus.ACTIVE);

        if (existingPeer.isPresent()) {
            // already connected — return existing config
            WireguardPeer peer = existingPeer.get();
            String config = wireguardConfigService.buildConfig(peer, peer.getServerNode());
            return buildResponse(peer, config);
        }

        // 2. pick server
        ServerNode server;
        if (request.getServerId() != null) {
            server = serverNodeRepository.findById(request.getServerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Server not found"));
            if (!server.isAvailable()) {
                throw new RuntimeException("Selected server is not available");
            }
        } else {
            server = serverNodeService.getBestAvailableServer();
        }

        // 3. generate keypair
        String[] keypair = wireguardConfigService.generateKeypair();
        String privateKey = keypair[0];
        String publicKey = keypair[1];

        // 4. assign IP
        String assignedIp = wireguardConfigService.assignNextIp();

        // 5. create peer record
        WireguardPeer peer = WireguardPeer.builder()
                .user(user)
                .serverNode(server)
                .privateKey(privateKey)
                .publicKey(publicKey)
                .assignedIp(assignedIp)
                .status(PeerStatus.ACTIVE)
                .lastSeenAt(Instant.now())
                .build();

        wireguardPeerRepository.save(peer);

        // 6. increment server peer count
        server.setCurrentPeers(server.getCurrentPeers() + 1);
        if (server.getCurrentPeers() >= server.getMaxPeers()) {
            server.setStatus(ServerStatus.FULL);
        }
        serverNodeRepository.save(server);

        // 7. build and return config
        String config = wireguardConfigService.buildConfig(peer, server);
        return buildResponse(peer, config);
    }

    @Transactional
    public void disconnect(User user) {
        WireguardPeer peer = wireguardPeerRepository
                .findByUserAndStatus(user, PeerStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active connection found"));

        // mark peer as disconnected
        peer.setStatus(PeerStatus.DISCONNECTED);
        peer.setLastSeenAt(Instant.now());
        wireguardPeerRepository.save(peer);

        // decrement server peer count
        ServerNode server = peer.getServerNode();
        server.setCurrentPeers(Math.max(0, server.getCurrentPeers() - 1));
        if (server.getStatus() == ServerStatus.FULL) {
            server.setStatus(ServerStatus.ACTIVE);
        }
        serverNodeRepository.save(server);
    }

    public VpnConnectResponseDto getStatus(User user) {
        WireguardPeer peer = wireguardPeerRepository
                .findByUserAndStatus(user, PeerStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active connection found"));

        String config = wireguardConfigService.buildConfig(peer, peer.getServerNode());
        return buildResponse(peer, config);
    }

    private VpnConnectResponseDto buildResponse(WireguardPeer peer, String config) {
        return VpnConnectResponseDto.builder()
                .peerId(peer.getId())
                .assignedIp(peer.getAssignedIp())
                .serverName(peer.getServerNode().getName())
                .serverCountry(peer.getServerNode().getCountry())
                .flagEmoji(peer.getServerNode().getFlagEmoji())
                .config(config)
                .build();
    }
}