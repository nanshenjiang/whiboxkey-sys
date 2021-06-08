package com.scnu.whiboxkey.pksys.service.impl;

import com.scnu.whiboxkey.pksys.models.ClientKey;
import com.scnu.whiboxkey.pksys.models.ServerKey;
import com.scnu.whiboxkey.pksys.repository.ClientKeyRepository;
import com.scnu.whiboxkey.pksys.service.ClientKeyService;
import com.scnu.whiboxkey.pksys.utils.BeanUtilsExt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class ClientKeyServiceImpl implements ClientKeyService {
    @Autowired
    private ClientKeyRepository clientKeyRepository;

    @Override
    public List<ClientKey> findAll() {
        return clientKeyRepository.findAll();
    }

    @Override
    public ClientKey findById(Long id) {
        Optional<ClientKey> optional = clientKeyRepository.findById(id);
        return optional.orElseGet(ClientKey::new);
    }

    @Override
    @Transactional
    public ClientKey saveClientKey(ClientKey clientKey) {
        return clientKeyRepository.save(clientKey);
    }

    @Override
    @Transactional
    public ClientKey updateClientKey(Long id, ClientKey clientKey) {
        ClientKey ck = this.findById(id);
        BeanUtils.copyProperties(clientKey, ck, BeanUtilsExt.getNullPropertyNames(clientKey));
        return clientKeyRepository.saveAndFlush(ck);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ClientKey clientKey = this.findById(id);
        String oldKfpath = clientKey.getEncKfpath();
        File oldkfile = new File(oldKfpath);
        if(oldkfile.exists()){
            oldkfile.delete();
        }
        oldKfpath = clientKey.getDecKfpath();
        oldkfile = new File(oldKfpath);
        if(oldkfile.exists()){
            oldkfile.delete();
        }
        clientKeyRepository.deleteById(id);
    }

    @Override
    public ClientKey findBySerial(String serial) {
        return clientKeyRepository.findBySerial(serial);
    }

    @Override
    public ClientKey findByPass(String pass) {
        return clientKeyRepository.findByPass(pass);
    }
}
