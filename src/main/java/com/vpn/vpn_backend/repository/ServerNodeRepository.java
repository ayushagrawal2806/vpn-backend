package com.vpn.vpn_backend.repository;

import com.vpn.vpn_backend.entity.ServerNode;
import com.vpn.vpn_backend.enums.ServerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServerNodeRepository extends JpaRepository<ServerNode, UUID> {
    List<ServerNode> findByStatus(ServerStatus status);
    List<ServerNode> findByCountry(String country);
    List<ServerNode> findByStatusOrderByCurrentPeersAsc(ServerStatus status);
}