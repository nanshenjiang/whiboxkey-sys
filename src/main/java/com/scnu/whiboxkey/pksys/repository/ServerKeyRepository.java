package com.scnu.whiboxkey.pksys.repository;

import com.scnu.whiboxkey.pksys.models.ServerKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerKeyRepository extends JpaRepository<ServerKey,Long> {
    ServerKey findBySerial(String serial);
}
