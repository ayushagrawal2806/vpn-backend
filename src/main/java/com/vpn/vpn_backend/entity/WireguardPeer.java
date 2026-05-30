package com.vpn.vpn_backend.entity;

import com.vpn.vpn_backend.enums.PeerStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "wireguard_peers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WireguardPeer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_node_id", nullable = false)
    private ServerNode serverNode;

    @Column(nullable = false)
    private String publicKey;

    @Column(nullable = false)
    private String privateKey;

    @Column(nullable = false, unique = true)
    private String assignedIp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeerStatus status;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    private Instant lastSeenAt;
}