package com.scnu.whiboxkey.pksys.service.impl;

import com.scnu.whiboxkey.pksys.models.ClientKey;
import com.scnu.whiboxkey.pksys.models.ServerKey;
import com.scnu.whiboxkey.pksys.repository.ClientKeyRepository;
import com.scnu.whiboxkey.pksys.repository.ServerKeyRepository;
import com.scnu.whiboxkey.pksys.service.ClientKeyService;
import com.scnu.whiboxkey.pksys.service.ServerKeyService;
import com.scnu.whiboxkey.pksys.utils.BeanUtilsExt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceKeyServiceImpl implements ServerKeyService {
    @Autowired
    private ServerKeyRepository serverKeyRepository;

    @Override
    public List<ServerKey> findAll() {
        return serverKeyRepository.findAll();
    }

    @Override
    public ServerKey findById(Long id) {
        Optional<ServerKey> optional = serverKeyRepository.findById(id);
        return optional.orElseGet(ServerKey::new);
    }

    @Override
    @Transactional
    public ServerKey saveServerKey(ServerKey serverKey) {
        return serverKeyRepository.save(serverKey);
    }

    @Override
    @Transactional
    public ServerKey updateServerKey(Long id, ServerKey serverKey) {
        ServerKey sk = this.findById(id);
        BeanUtils.copyProperties(serverKey, sk, BeanUtilsExt.getNullPropertyNames(serverKey));
        return serverKeyRepository.saveAndFlush(sk);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        serverKeyRepository.deleteById(id);
    }

    @Override
    public ServerKey findBySerial(String serial) {
        return serverKeyRepository.findBySerial(serial);
    }
}
