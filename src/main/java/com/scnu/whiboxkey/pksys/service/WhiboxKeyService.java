package com.scnu.whiboxkey.pksys.service;

import com.scnu.whiboxkey.pksys.models.WhiboxKey;

import java.util.List;

public interface WhiboxKeyService {
    List<WhiboxKey> findAll();

    WhiboxKey findById(Long id);

    WhiboxKey save(WhiboxKey whiboxKey);

    WhiboxKey update (Long id, WhiboxKey whiboxKey);

    void deleteById(Long id);

    WhiboxKey findByPass(String pass);
}
