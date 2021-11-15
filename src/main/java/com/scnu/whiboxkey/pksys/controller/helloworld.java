package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.crypto.SM4EncKey;
import com.scnu.whiboxkey.pksys.crypto.Sm4EncCBC;
import com.scnu.whiboxkey.pksys.crypto.Sm4EncGCM;
import com.scnu.whiboxkey.pksys.utils.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/whibox")
public class helloworld {

    @GetMapping("/hello")
    public JSONResult helloworld() {
        return JSONResult.ok("helloworld");
    }

    @GetMapping("/test/enc/key")
    public JSONResult test_enc_key() throws UnsupportedEncodingException {
        String key = RandomUtils.randomString(16);
        System.out.println("原始密钥："+key+" length:"+key.length());
        String enckey = SM4EncKey.KeystoreSM4EncKey(key);
        System.out.println("加密后密钥："+enckey+" length:"+key.length());
        String deckey = SM4EncKey.KeystoreSM4DecKey(enckey);
        System.out.println("解密后密钥："+deckey+" length:"+key.length());
        return JSONResult.ok("ok");
    }

    @GetMapping("/test/enc/algs")
    public JSONResult test_enc_algs(){
        //cbc
        Sm4EncCBC cbc_enc = new Sm4EncCBC("1234567890abcdef", "00", "000102030405060708090a0b0c0d0eff");
        cbc_enc.sm4EncCbcFun();
        String cipher1 = cbc_enc.getAns();
        System.out.println("sm4-cbc加密结果："+cipher1);
        Sm4EncCBC cbc_dec = new Sm4EncCBC("1234567890abcdef", "00", cipher1);
        cbc_dec.sm4DecCbcFun();
        System.out.println("sm4-cbc解密结果："+cbc_dec.getAns());
        //gcm
        Sm4EncGCM gcm_enc = new Sm4EncGCM("1234567890abcdef", "000102030405060708090a0b0c0d0e0f", "000102030405060708090a0b0c0d0e0f");
        gcm_enc.sm4EncGcmFun();
        String cipher2 = gcm_enc.getAns();
        System.out.println("sm4-gcm加密结果："+cipher2);
        System.out.println("sm4-gcm加密tag值："+gcm_enc.getTag());
        Sm4EncGCM gcm_dec = new Sm4EncGCM("1234567890abcdef", "000102030405060708090a0b0c0d0e0f", cipher2);
        gcm_dec.sm4DecGcmFun();
        System.out.println("sm4-gcm解密结果："+gcm_dec.getAns());
        System.out.println("sm4-gcm解密tag值："+gcm_dec.getTag());
        return JSONResult.ok("ok");
    }

    @GetMapping("/test/token")
    public JSONResult test_token(HttpServletResponse response){
        String token = JWTUtils.createToken("test","192.168.1.1");
        System.out.println(token);
        Boolean flag = JWTUtils.validateToken(token, "test","192.168.1.1");
        System.out.println(flag);
        response.setHeader(JWTUtils.AUTH_TOKEN, token);
        return JSONResult.ok(flag);
    }
}
