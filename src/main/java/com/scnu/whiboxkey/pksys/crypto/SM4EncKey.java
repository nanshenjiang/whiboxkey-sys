package com.scnu.whiboxkey.pksys.crypto;

import com.scnu.whiboxkey.pksys.utils.ByteUtil;

import java.nio.charset.StandardCharsets;

/**
 * 密钥安全存储，内部版本使用，需先加密再解密
 */
public class SM4EncKey {

    private static String sm4_key = "aisino+scnu";

    /**
     * 使用密钥sm4_key，对输入的明文密钥进行加密，用于安全存储，
     * @param origin_key 原始是普通String类型，长度为16
     * @return 返回加密后的密钥
     */
    public static String KeystoreSM4EncKey(String origin_key){
        byte[] sm4_key_byte = sm4_key.getBytes(StandardCharsets.UTF_8);
        byte[] okey_byte = origin_key.getBytes(StandardCharsets.UTF_8);
        byte[] cipher = new byte[16];
        WBCryptolib.WBCRYPTO_sm4_context sm4_context = WBCryptolib.INSTANCE.WBCRYPTO_sm4_context_init();
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_init_key(sm4_context, sm4_key_byte, sm4_key_byte.length);
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_encrypt(okey_byte, cipher, sm4_context);
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_context_free(sm4_context);
        return ByteUtil.byteArrayToHexStr(cipher);
    }

    /**
     * 使用密钥sm4_key，对输入的加密密钥进行解密
     * @param enc_key 加密后的密钥是16进制版本的String
     * @return 返回解密后的密钥
     */
    public static String KeystoreSM4DecKey(String enc_key){
        byte[] sm4_key_byte = sm4_key.getBytes(StandardCharsets.UTF_8);
        byte[] ekey_byte = ByteUtil.hexStrToByteArray(enc_key);
        byte[] plain = new byte[16];
        WBCryptolib.WBCRYPTO_sm4_context sm4_context = WBCryptolib.INSTANCE.WBCRYPTO_sm4_context_init();
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_init_key(sm4_context, sm4_key_byte, sm4_key_byte.length);
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_decrypt(ekey_byte, plain, sm4_context);
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_context_free(sm4_context);
        return new String(plain);
    }
 }
