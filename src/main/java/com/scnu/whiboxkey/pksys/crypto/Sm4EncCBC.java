package com.scnu.whiboxkey.pksys.crypto;

import com.scnu.whiboxkey.pksys.utils.ByteUtil;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.nio.charset.StandardCharsets;

/**
 * 黑盒sm4-CBC模式
 */
public class Sm4EncCBC {

    //密钥
    private String key;

    //初始值iv，需为16进制String
    private String iv;

    //需要处理的信息，需为16进制String
    private String text;

    //处理后的结果
    private String ans;

    public Sm4EncCBC(String key, String iv, String text) {
        this.key = key;
        this.iv = iv;
        this.text = text;
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

    /**
     * 使用sm4-cbc对消息进行加密
     */
    public void sm4EncCbcFun(){
        byte[] key_byte = this.key.getBytes(StandardCharsets.UTF_8);
        byte[] ivbyte = ByteUtil.hexStrToByteArray(this.iv);
        byte[] tbyte = ByteUtil.hexStrToByteArray(this.text);
        //保证加密消息的空间够
        int tbyte_len = tbyte.length;
        if(tbyte.length%16!=0){
            tbyte_len = tbyte_len - (tbyte_len%16) + 16;
        }else {
            tbyte_len += 16;
        }
        byte[] ans_byte = new byte[tbyte_len];
        IntByReference use_len = new IntByReference(0);
        WBCryptolib.WBCRYPTO_sm4_context sm4_context = WBCryptolib.INSTANCE.WBCRYPTO_sm4_context_init();
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_init_key(sm4_context, key_byte, key_byte.length);
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_cbc_encrypt(tbyte, tbyte.length, ans_byte, ans_byte.length, use_len, sm4_context, ivbyte);
        this.ans = ByteUtil.byteArrayToHexStr(ans_byte, use_len.getValue());
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_context_free(sm4_context);
//        long peer = Pointer.nativeValue(use_len.getPointer());
//        Native.free(peer);
    }

    /**
     * 使用sm4-cbc对消息进行解密
     */
    public void sm4DecCbcFun(){
        byte[] key_byte = this.key.getBytes(StandardCharsets.UTF_8);
        byte[] ivbyte = ByteUtil.hexStrToByteArray(this.iv);
        byte[] tbyte = ByteUtil.hexStrToByteArray(this.text);
        byte[] ans_byte = new byte[tbyte.length];
        IntByReference use_len = new IntByReference(0);
        WBCryptolib.WBCRYPTO_sm4_context sm4_context = WBCryptolib.INSTANCE.WBCRYPTO_sm4_context_init();
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_init_key(sm4_context, key_byte, key_byte.length);
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_cbc_decrypt(tbyte, tbyte.length, ans_byte, ans_byte.length, use_len, sm4_context, ivbyte);
        this.ans = ByteUtil.byteArrayToHexStr(ans_byte, use_len.getValue());
        WBCryptolib.INSTANCE.WBCRYPTO_sm4_context_free(sm4_context);
//        long peer = Pointer.nativeValue(use_len.getPointer());
//        Native.free(peer);
    }
}
