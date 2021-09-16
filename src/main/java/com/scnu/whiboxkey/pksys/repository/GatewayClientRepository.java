package com.scnu.whiboxkey.pksys.repository;

import com.scnu.whiboxkey.pksys.models.GatewayClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GatewayClientRepository extends JpaRepository<GatewayClient,Long>, JpaSpecificationExecutor<GatewayClient> {
    GatewayClient findBySerial(String serial);
}
