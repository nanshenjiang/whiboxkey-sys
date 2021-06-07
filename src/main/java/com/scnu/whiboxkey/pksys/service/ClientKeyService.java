package com.scnu.whiboxkey.pksys.service;

import com.scnu.whiboxkey.pksys.models.ClientKey;

import java.util.List;

public interface ClientKeyService {
    List<ClientKey> findAll();

    ClientKey findById(Long id);

    ClientKey saveClientKey(ClientKey clientKey);

    ClientKey updateClientKey(Long id, ClientKey clientKey);

    void deleteById(Long id);

    ClientKey findBySerial(String serial);

    ClientKey findByPass(String pass);
}
