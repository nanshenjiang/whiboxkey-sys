package com.scnu.whiboxkey.pksys.repository;

import com.scnu.whiboxkey.pksys.models.GatewayClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatewayClientRepository extends JpaRepository<GatewayClient,Long> {
    GatewayClient findBySerial(String serial);
}
