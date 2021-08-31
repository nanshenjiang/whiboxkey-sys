package com.scnu.whiboxkey.pksys.service;

import com.scnu.whiboxkey.pksys.models.GatewayClient;
import com.scnu.whiboxkey.pksys.models.KeyMsg;

import java.util.List;

public interface GatewayClientService {
    List<GatewayClient> findAll();

    GatewayClient findById(Long id);

    GatewayClient save(GatewayClient gatewayClient);

    GatewayClient update(Long id, GatewayClient gatewayClient);

    void deleteById(Long id);

    GatewayClient findBySerial(String serial);

    KeyMsg getUpKey(Long id);

    KeyMsg getDownKey(Long id);
}
