package com.scnu.whiboxkey.pksys.service.impl;

import com.scnu.whiboxkey.pksys.models.GatewayClient;
import com.scnu.whiboxkey.pksys.models.GatewayServer;
import com.scnu.whiboxkey.pksys.repository.GatewayServerRepository;
import com.scnu.whiboxkey.pksys.service.GatewayClientService;
import com.scnu.whiboxkey.pksys.service.GatewayServerService;
import com.scnu.whiboxkey.pksys.utils.BeanUtilsExt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class GatewayServiceServiceImpl implements GatewayServerService {
    @Autowired
    private GatewayServerRepository serverKeyRepository;

    @Autowired
    private GatewayClientService clientKeyService;

    @Override
    public List<GatewayServer> findAll() {
        return serverKeyRepository.findAll();
    }

    @Override
    public GatewayServer findById(Long id) {
        Optional<GatewayServer> optional = serverKeyRepository.findById(id);
        return optional.orElseGet(GatewayServer::new);
    }

    @Override
    @Transactional
    public GatewayServer save(GatewayServer gatewayServer) {
        return serverKeyRepository.save(gatewayServer);
    }

    @Override
    @Transactional
    public GatewayServer update(Long id, GatewayServer gatewayServer) {
        GatewayServer sk = this.findById(id);
        BeanUtils.copyProperties(gatewayServer, sk, BeanUtilsExt.getNullPropertyNames(gatewayServer));
        return serverKeyRepository.saveAndFlush(sk);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        GatewayServer sk = this.findById(id);
        Collection<GatewayClient> ckCollection = sk.getClientKeyList();
        for(GatewayClient it: ckCollection){
            clientKeyService.deleteById(it.getId());
        }
        serverKeyRepository.deleteById(id);
    }

    @Override
    public GatewayServer findBySerial(String serial) {
        return serverKeyRepository.findBySerial(serial);
    }
}
