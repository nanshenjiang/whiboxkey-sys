package com.scnu.whiboxkey.pksys.repository;

import com.scnu.whiboxkey.pksys.models.KeyMsg;
import com.scnu.whiboxkey.pksys.models.WhiboxKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WhiboxKeyRepository extends JpaRepository<WhiboxKey,Long> {
    WhiboxKey findByPass(String pass);

    WhiboxKey findByIdAndVersion(Long id, String version);
}
