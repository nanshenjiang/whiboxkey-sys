package com.scnu.whiboxkey.pksys.service;

import com.scnu.whiboxkey.pksys.models.ServerKey;

import java.util.List;

public interface ServerKeyService {
    List<ServerKey> findAll();

    ServerKey findById(Long id);

    ServerKey saveServerKey(ServerKey serverKey);

    ServerKey updateServerKey(Long id, ServerKey serverKey);

    void deleteById(Long id);

    ServerKey findBySerial(String serial);
}
