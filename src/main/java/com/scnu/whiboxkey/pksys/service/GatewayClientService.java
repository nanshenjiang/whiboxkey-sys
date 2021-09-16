package com.scnu.whiboxkey.pksys.service;

import com.scnu.whiboxkey.pksys.models.GatewayClient;
import com.scnu.whiboxkey.pksys.models.GatewayServer;
import com.scnu.whiboxkey.pksys.models.KeyMsg;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GatewayClientService {
    Page<GatewayClient> findGCNoCriteria(Integer page, Integer size);

    List<GatewayClient> findAll();

    GatewayClient findById(Long id);

    GatewayClient save(GatewayClient gatewayClient);

    GatewayClient update(Long id, GatewayClient gatewayClient);

    void deleteById(Long id);

    GatewayClient findBySerial(String serial);

    KeyMsg getUpKey(Long id);

    KeyMsg getDownKey(Long id);
}
