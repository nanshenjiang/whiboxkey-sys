package com.scnu.whiboxkey.pksys.service.impl;

import com.scnu.whiboxkey.pksys.models.GatewayClient;
import com.scnu.whiboxkey.pksys.models.KeyMsg;
import com.scnu.whiboxkey.pksys.models.WhiboxKey;
import com.scnu.whiboxkey.pksys.repository.KeyMsgRepository;
import com.scnu.whiboxkey.pksys.service.KeyMsgService;
import com.scnu.whiboxkey.pksys.service.WhiboxKeyService;
import com.scnu.whiboxkey.pksys.utils.BeanUtilsExt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class KeyMsgServiceImpl implements KeyMsgService {
    @Autowired
    private KeyMsgRepository keyMsgRepository;

    @Autowired
    private WhiboxKeyService whiboxKeyService;

    @Override
    public List<KeyMsg> findAll() {
        return keyMsgRepository.findAll();
    }

    @Override
    public KeyMsg findById(Long id) {
        Optional<KeyMsg> optional = keyMsgRepository.findById(id);
        return optional.orElseGet(KeyMsg::new);
    }

    @Override
    @Transactional
    public KeyMsg save(KeyMsg keyMsg) {
        return keyMsgRepository.save(keyMsg);
    }

    @Override
    @Transactional
    public KeyMsg update(Long id, KeyMsg keyMsg) {
        KeyMsg wk = this.findById(id);
        BeanUtils.copyProperties(keyMsg, wk, BeanUtilsExt.getNullPropertyNames(keyMsg));
        return keyMsgRepository.saveAndFlush(wk);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        KeyMsg km = this.findById(id);
        Collection<WhiboxKey> whiboxKeyCollection = km.getWhiboxKeyList();
        for(WhiboxKey it: whiboxKeyCollection){
            whiboxKeyService.deleteById(it.getId());
        }
        keyMsgRepository.deleteById(id);
    }

    /**
     * 查找白盒表中最新版本的whiboxkey
     */
    @Override
    public WhiboxKey findLatestVersion(Long id) {
        KeyMsg km = this.findById(id);
        WhiboxKey ret = null;
        Collection<WhiboxKey> whiboxKeyCollection = km.getWhiboxKeyList();
        long sort = -1;
        for(WhiboxKey it: whiboxKeyCollection){
            if(it.getVersion() > sort){
                ret = it;
                sort = it.getVersion();
            }
        }
        return ret;
    }

    /**
     * 根据版本号查找whiboxkey
     */
    @Override
    public WhiboxKey findByVersion(Long id, Long version) {
        KeyMsg km = this.findById(id);
        WhiboxKey ret = null;
        Collection<WhiboxKey> whiboxKeyCollection = km.getWhiboxKeyList();
        for(WhiboxKey it: whiboxKeyCollection){
            if(it.getVersion() == version){
                ret = it;
            }
        }
        return ret;
    }

    /**
     * 插入新的白盒密钥表
     */
    @Override
    @Transactional
    public KeyMsg updateByWhiboxKey(Long id, WhiboxKey whiboxKey) {
        KeyMsg wk = this.findById(id);
        KeyMsg updateWk = this.findById(id);
        updateWk.getWhiboxKeyList().add(whiboxKey);
        BeanUtils.copyProperties(updateWk, wk, BeanUtilsExt.getNullPropertyNames(updateWk));
        return keyMsgRepository.saveAndFlush(wk);
    }
}
