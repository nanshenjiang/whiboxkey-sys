package com.scnu.whiboxkey.pksys.repository;

import com.scnu.whiboxkey.pksys.models.ClientKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientKeyRepository extends JpaRepository<ClientKey,Long> {
    ClientKey findBySerial(String serial);

    ClientKey findByPass(String pass);
}
