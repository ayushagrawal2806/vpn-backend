package com.vpn.vpn_backend.repository;

import com.vpn.vpn_backend.entity.WireguardPeer;
import com.vpn.vpn_backend.entity.User;
import com.vpn.vpn_backend.enums.PeerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WireguardPeerRepository extends JpaRepository<WireguardPeer, UUID> {

    Optional<WireguardPeer> findByUserAndStatus(User user, PeerStatus status);

    List<WireguardPeer> findByUser(User user);

    boolean existsByAssignedIp(String assignedIp);

    @Query("SELECT w.assignedIp FROM WireguardPeer w ORDER BY w.createdAt DESC")
    List<String> findAllAssignedIps();
}