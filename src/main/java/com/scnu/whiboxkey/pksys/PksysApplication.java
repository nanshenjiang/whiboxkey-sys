package com.scnu.whiboxkey.pksys;

import com.scnu.whiboxkey.pksys.utils.JWTUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        JWTUtils.class,
})
public class PksysApplication {

    public static void main(String[] args) {
        SpringApplication.run(PksysApplication.class, args);
    }

}
