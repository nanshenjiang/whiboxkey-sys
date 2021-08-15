package com.scnu.whiboxkey.pksys.service.impl;

import com.scnu.whiboxkey.pksys.models.GatewayClient;
import com.scnu.whiboxkey.pksys.models.WhiboxKey;
import com.scnu.whiboxkey.pksys.repository.GatewayClientRepository;
import com.scnu.whiboxkey.pksys.repository.WhiboxKeyRepository;
import com.scnu.whiboxkey.pksys.service.GatewayClientService;
import com.scnu.whiboxkey.pksys.service.WhiboxKeyService;
import com.scnu.whiboxkey.pksys.utils.BeanUtilsExt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class GatewayClientServiceImpl implements GatewayClientService {
    @Autowired
    private GatewayClientRepository clientKeyRepository;

    @Autowired
    private WhiboxKeyService whiboxKeyService;

    @Override
    public List<GatewayClient> findAll() {
        return clientKeyRepository.findAll();
    }

    @Override
    public GatewayClient findById(Long id) {
        Optional<GatewayClient> optional = clientKeyRepository.findById(id);
        return optional.orElseGet(GatewayClient::new);
    }

    @Override
    @Transactional
    public GatewayClient save(GatewayClient gatewayClient) {
        return clientKeyRepository.save(gatewayClient);
    }

    @Override
    @Transactional
    public GatewayClient update(Long id, GatewayClient gatewayClient) {
        GatewayClient ck = this.findById(id);
        BeanUtils.copyProperties(gatewayClient, ck, BeanUtilsExt.getNullPropertyNames(gatewayClient));
        return clientKeyRepository.saveAndFlush(ck);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        GatewayClient clientKey = this.findById(id);
        Collection<WhiboxKey> whiboxKeyCollection = clientKey.getWhiboxKeyList();
        for(WhiboxKey it: whiboxKeyCollection){
            whiboxKeyService.deleteById(it.getId());
        }
        clientKeyRepository.deleteById(id);
    }

    @Override
    public GatewayClient findBySerial(String serial) {
        return clientKeyRepository.findBySerial(serial);
    }
}
