package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.crypto.SM4EncKey;
import com.scnu.whiboxkey.pksys.crypto.Sm4EncCBC;
import com.scnu.whiboxkey.pksys.crypto.Sm4EncGCM;
import com.scnu.whiboxkey.pksys.crypto.WBCryptolib;
import com.scnu.whiboxkey.pksys.utils.ByteUtil;
import com.scnu.whiboxkey.pksys.utils.JSONResult;
import com.scnu.whiboxkey.pksys.utils.RandomUtils;
import com.sun.jna.ptr.IntByReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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
        Sm4EncCBC cbc_enc = new Sm4EncCBC("1234567890abcdef", "000102030405060708090a0b0c0d0e0f", "0123456789abcdef");
        cbc_enc.sm4EncCbcFun();
        String cipher1 = cbc_enc.getAns();
        System.out.println("sm4-cbc加密结果："+cipher1);
        Sm4EncCBC cbc_dec = new Sm4EncCBC("1234567890abcdef", "000102030405060708090a0b0c0d0e0f", cipher1);
        cbc_dec.sm4DecCbcFun();
        System.out.println("sm4-cbc解密结果："+cbc_dec.getAns());
        //gcm
        Sm4EncGCM gcm_enc = new Sm4EncGCM("1234567890abcdef", "000102030405060708090a0b0c0d0e0f","000102030405060708090a0b0c0d0e0f", "0123456789abcdef");
        gcm_enc.sm4EncGcmFun();
        String cipher2 = gcm_enc.getAns();
        System.out.println("sm4-gcm加密结果："+cipher2);
        System.out.println("sm4-gcm加密tag值："+gcm_enc.getTag());
        Sm4EncGCM gcm_dec = new Sm4EncGCM("1234567890abcdef", "000102030405060708090a0b0c0d0e0f","000102030405060708090a0b0c0d0e0f", cipher2);
        gcm_dec.sm4DecGcmFun();
        System.out.println("sm4-gcm解密结果："+gcm_dec.getAns());
        System.out.println("sm4-gcm解密tag值："+gcm_dec.getTag());
//        byte[] key_byte = "123456789".getBytes(StandardCharsets.UTF_8);
//        WBCryptolib.WBCRYPTO_sm4_context sm4_context = WBCryptolib.INSTANCE.WBCRYPTO_sm4_context_init();
//        WBCryptolib.INSTANCE.WBCRYPTO_sm4_init_key(sm4_context, key_byte, key_byte.length);
//        WBCryptolib.WBCRYPTO_wbsm4_context wbsm4_context = WBCryptolib.INSTANCE.WBCRYPTO_wbsm4_context_init(1,1);
//        WBCryptolib.INSTANCE.WBCRYPTO_wbsm4_gen_table(wbsm4_context, key_byte, key_byte.length);
//        System.out.println("11111");
        return JSONResult.ok("ok");
    }
}
