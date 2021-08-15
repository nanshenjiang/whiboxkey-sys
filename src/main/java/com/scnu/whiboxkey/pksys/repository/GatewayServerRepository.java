package com.scnu.whiboxkey.pksys.repository;

import com.scnu.whiboxkey.pksys.models.GatewayServer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatewayServerRepository extends JpaRepository<GatewayServer,Long> {
    GatewayServer findBySerial(String serial);
}
