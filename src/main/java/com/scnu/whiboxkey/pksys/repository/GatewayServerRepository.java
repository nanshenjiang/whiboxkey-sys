package com.scnu.whiboxkey.pksys.repository;

import com.scnu.whiboxkey.pksys.models.GatewayServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GatewayServerRepository extends JpaRepository<GatewayServer,Long>, JpaSpecificationExecutor<GatewayServer> {
    GatewayServer findBySerial(String serial);
}
