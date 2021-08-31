package com.scnu.whiboxkey.pksys.crypto;

import com.scnu.whiboxkey.pksys.utils.ByteUtil;
import com.sun.jna.Native;

import java.nio.charset.StandardCharsets;

/**
 * 黑盒sm4-gcm模式
 */
public class Sm4EncGCM {

    //密钥
    private String key;

    //初始值iv，需为16进制String
    private String iv;

    //需要处理的信息，需为16进制String
    private String text;

    //处理后的结果
    private String ans;

    //消息认证码
    private String tag;

    public Sm4EncGCM(String key, String iv, String text) {
        this.key = key;
        this.iv = iv;
        this.text = text;
    }

    /**
     * 使用sm4-gcm对消息进行加密
     */
    public void sm4EncGcmFun(){
        byte[] key_byte = this.key.getBytes(StandardCharsets.UTF_8);
        byte[] ivbyte = ByteUtil.hexStrToByteArray(this.iv);
        byte[] tbyte = ByteUtil.hexStrToByteArray(this.text);
        byte[] ans_byte = new byte[tbyte.length];
        byte[] tag = new byte[16];
        WBCryptolib.WBCRYPTO_sm4_context sm4_context = WBCryptolib.INSTANCE.WBCRYPTO_sm4_context_init();
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_init_key(sm4_context, key_byte, key_byte.length);
        WBCryptolib.WBCRYPTO_gcm_context gcm_ctx = WBCryptolib.INSTANCE.WBCRYPTO_sm4_gcm_init(sm4_context);
        WBCryptolib.INSTANCE.WBCRYPTO_gcm_setiv(gcm_ctx, ivbyte, ivbyte.length);
        WBCryptolib.INSTANCE.WBCRYPTO_gcm_encrypt(gcm_ctx, tbyte, tbyte.length, ans_byte, ans_byte.length);
        WBCryptolib.INSTANCE.WBCRYPTO_gcm_finish(gcm_ctx, tag, tag.length);
        WBCryptolib.INSTANCE.WBCRYPTO_gcm_free(gcm_ctx);
        this.ans = ByteUtil.byteArrayToHexStr(ans_byte);
        this.tag = ByteUtil.byteArrayToHexStr(tag);
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_context_free(sm4_context);
    }

    /**
     * 使用sm4-gcm对消息进行解密
     */
    public void sm4DecGcmFun(){
        byte[] key_byte = this.key.getBytes(StandardCharsets.UTF_8);
        byte[] ivbyte = ByteUtil.hexStrToByteArray(this.iv);
        byte[] tbyte = ByteUtil.hexStrToByteArray(this.text);
        byte[] ans_byte = new byte[tbyte.length];
        byte[] tag = new byte[16];
        WBCryptolib.WBCRYPTO_sm4_context sm4_context = WBCryptolib.INSTANCE.WBCRYPTO_sm4_context_init();
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_init_key(sm4_context, key_byte, key_byte.length);
        WBCryptolib.WBCRYPTO_gcm_context gcm_ctx = WBCryptolib.INSTANCE.WBCRYPTO_sm4_gcm_init(sm4_context);
        WBCryptolib.INSTANCE.WBCRYPTO_gcm_setiv(gcm_ctx, ivbyte, ivbyte.length);
        WBCryptolib.INSTANCE.WBCRYPTO_gcm_decrypt(gcm_ctx, tbyte, tbyte.length, ans_byte, ans_byte.length);
        WBCryptolib.INSTANCE.WBCRYPTO_gcm_finish(gcm_ctx, tag, tag.length);
        WBCryptolib.INSTANCE.WBCRYPTO_gcm_free(gcm_ctx);
        this.ans = ByteUtil.byteArrayToHexStr(ans_byte);
        this.tag = ByteUtil.byteArrayToHexStr(tag);
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_context_free(sm4_context);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
