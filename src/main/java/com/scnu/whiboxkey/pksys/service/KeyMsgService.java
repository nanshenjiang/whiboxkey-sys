package com.scnu.whiboxkey.pksys.service;

import com.scnu.whiboxkey.pksys.models.KeyMsg;
import com.scnu.whiboxkey.pksys.models.WhiboxKey;

import java.util.List;

public interface KeyMsgService {
    List<KeyMsg> findAll();

    KeyMsg findById(Long id);

    KeyMsg save(KeyMsg keyMsg);

    KeyMsg update (Long id, KeyMsg keyMsg);

    void deleteById(Long id);

    WhiboxKey findLatestVersion(Long id);

    WhiboxKey findByVersion(Long id, Long version);

    KeyMsg updateByWhiboxKey(Long id, WhiboxKey whiboxKey);
}
