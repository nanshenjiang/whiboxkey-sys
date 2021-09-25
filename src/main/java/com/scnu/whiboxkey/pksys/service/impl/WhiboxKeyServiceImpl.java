package com.scnu.whiboxkey.pksys.service.impl;

import com.scnu.whiboxkey.pksys.models.KeyMsg;
import com.scnu.whiboxkey.pksys.models.WhiboxKey;
import com.scnu.whiboxkey.pksys.repository.WhiboxKeyRepository;
import com.scnu.whiboxkey.pksys.service.WhiboxKeyService;
import com.scnu.whiboxkey.pksys.utils.BeanUtilsExt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class WhiboxKeyServiceImpl implements WhiboxKeyService {

    @Autowired
    private WhiboxKeyRepository whiboxKeyRepository;

    @Override
    public List<WhiboxKey> findAll() {
        return whiboxKeyRepository.findAll();
    }

    @Override
    public WhiboxKey findById(Long id) {
        Optional<WhiboxKey> optional = whiboxKeyRepository.findById(id);
        return optional.orElseGet(WhiboxKey::new);
    }

    @Override
    @Transactional
    public WhiboxKey save(WhiboxKey whiboxKey) {
        return whiboxKeyRepository.save(whiboxKey);
    }

    @Override
    @Transactional
    public WhiboxKey update(Long id, WhiboxKey whiboxKey) {
        WhiboxKey wk = this.findById(id);
        BeanUtils.copyProperties(whiboxKey, wk, BeanUtilsExt.getNullPropertyNames(whiboxKey));
        return whiboxKeyRepository.saveAndFlush(wk);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        WhiboxKey wk = this.findById(id);
        if (wk.getKeyFpath() != null) {
            String oldKfpath = wk.getKeyFpath();
            File oldkfile = new File(oldKfpath);
            if (oldkfile.exists()) {
                oldkfile.delete();
            }
        }
        whiboxKeyRepository.deleteById(id);
    }

    @Override
    public WhiboxKey findByIdAndVersion(Long id, String version) {
        return whiboxKeyRepository.findByIdAndVersion(id, version);
    }

    @Override
    public WhiboxKey findByPass(String pass) {
        return whiboxKeyRepository.findByPass(pass);
    }
}
