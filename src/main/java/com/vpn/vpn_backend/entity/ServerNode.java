package com.vpn.vpn_backend.entity;

import com.vpn.vpn_backend.enums.ServerStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "server_nodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServerNode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false, unique = true)
    private String ipAddress;

    @Column(nullable = false)
    private Integer port;

    @Column(nullable = false)
    private String publicKey;

    @Column(nullable = false)
    private Integer maxPeers;

    @Column(nullable = false)
    private Integer currentPeers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServerStatus status;

    @Column(nullable = false)
    private String flagEmoji;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public int getLoadPercentage() {
        if (maxPeers == 0) return 100;
        return (currentPeers * 100) / maxPeers;
    }

    public boolean isAvailable() {
        return status == ServerStatus.ACTIVE && currentPeers < maxPeers;
    }
}