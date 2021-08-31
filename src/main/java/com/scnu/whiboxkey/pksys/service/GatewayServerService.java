package com.scnu.whiboxkey.pksys.service;

import com.scnu.whiboxkey.pksys.models.GatewayClient;
import com.scnu.whiboxkey.pksys.models.GatewayServer;

import java.util.List;

public interface GatewayServerService {
    List<GatewayServer> findAll();

    GatewayServer findById(Long id);

    GatewayServer save(GatewayServer gatewayServer);

    GatewayServer update(Long id, GatewayServer gatewayServer);

    void deleteById(Long id);

    GatewayServer findBySerial(String serial);

    GatewayClient findByClientSerial(Long id, String serial);
}
