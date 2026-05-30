package com.vpn.vpn_backend.service;

import com.vpn.vpn_backend.entity.ServerNode;
import com.vpn.vpn_backend.entity.WireguardPeer;
import com.vpn.vpn_backend.repository.WireguardPeerRepository;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.crypto.params.X25519PrivateKeyParameters;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WireguardConfigService {

    private final WireguardPeerRepository wireguardPeerRepository;

    public String[] generateKeypair() {
        SecureRandom random = new SecureRandom();
        byte[] privateKeyBytes = new byte[32];
        random.nextBytes(privateKeyBytes);

        // clamp private key per WireGuard spec
        privateKeyBytes[0] &= 248;
        privateKeyBytes[31] &= 127;
        privateKeyBytes[31] |= 64;

        X25519PrivateKeyParameters privateKeyParams =
                new X25519PrivateKeyParameters(privateKeyBytes, 0);

        byte[] publicKeyBytes = privateKeyParams.generatePublicKey().getEncoded();

        String privateKey = Base64.getEncoder().encodeToString(privateKeyBytes);
        String publicKey = Base64.getEncoder().encodeToString(publicKeyBytes);

        return new String[]{privateKey, publicKey};
    }

    public String assignNextIp() {
        List<String> existingIps = wireguardPeerRepository.findAllAssignedIps();

        int maxOctet = 1;
        for (String ip : existingIps) {
            String[] parts = ip.split("\\.");
            int octet = Integer.parseInt(parts[3]);
            if (octet > maxOctet) maxOctet = octet;
        }

        int nextOctet = maxOctet + 1;
        if (nextOctet > 254) {
            throw new RuntimeException("VPN subnet is full");
        }

        return "10.0.0." + nextOctet;
    }

    public String buildConfig(WireguardPeer peer, ServerNode server) {
        return "[Interface]\n" +
                "PrivateKey = " + peer.getPrivateKey() + "\n" +
                "Address = " + peer.getAssignedIp() + "/32\n" +
                "DNS = 1.1.1.1\n" +
                "\n" +
                "[Peer]\n" +
                "PublicKey = " + server.getPublicKey() + "\n" +
                "Endpoint = " + server.getIpAddress() + ":" + server.getPort() + "\n" +
                "AllowedIPs = 0.0.0.0/0\n" +
                "PersistentKeepalive = 25\n";
    }
}