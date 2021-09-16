package com.scnu.whiboxkey.pksys.crypto;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import java.util.Arrays;
import java.util.List;

public interface WBCryptolib extends Library {

    WBCryptolib INSTANCE = (WBCryptolib) Native.loadLibrary("libwbcrypto", WBCryptolib.class);

    /***********************************************sm3***********************************************/
    int WBCRYPTO_sm3(byte[] msg, int msglen, byte[] digest);

    /*******************************************gcm context**********************************************/
    public static interface Block extends Callback {
        int block128_f(byte[] in, byte[] out, Pointer key);
    }
    public static class WBCRYPTO_gcm_context extends Structure{
        public WBCRYPTO_gcm_context(){}
        public long len;
        public long add_len;
        public long[] HL = new long[16];
        public long[] HH = new long[16];
        public byte[] base_ectr = new byte[16];
        public byte[] y = new byte[16];
        public byte[] buf = new byte[16];
        public Pointer key;
        public WBCryptolib.Block block;
        public static class ByReference extends WBCryptolib.WBCRYPTO_gcm_context implements Structure.ByReference{}
        public static class ByValue extends WBCryptolib.WBCRYPTO_gcm_context implements Structure.ByValue{}
        @Override
        protected List<String> getFieldOrder(){
            return Arrays.asList("len", "add_len", "HL", "HH", "base_ectr", "y", "buf", "key", "block");
        }
    }
    int WBCRYPTO_gcm_setiv(WBCryptolib.WBCRYPTO_gcm_context ctx, byte[] iv, int len);
    int WBCRYPTO_gcm_aad(WBCryptolib.WBCRYPTO_gcm_context ctx, byte[] aad, int len);
    int WBCRYPTO_gcm_encrypt(WBCryptolib.WBCRYPTO_gcm_context ctx,
                             byte[] in, int inlen,
                             byte[] out, int outlen);
    int WBCRYPTO_gcm_decrypt(WBCryptolib.WBCRYPTO_gcm_context ctx,
                             byte[] in, int inlen,
                             byte[] out, int outlen);
    int WBCRYPTO_gcm_finish(WBCryptolib.WBCRYPTO_gcm_context ctx, byte[] tag, int len);
    void WBCRYPTO_gcm_free(WBCryptolib.WBCRYPTO_gcm_context ctx);

    /*******************************************gcmfile context**********************************************/
    public static class WBCRYPTO_gcmfile_context extends Structure{
        public WBCRYPTO_gcmfile_context(){}
        public WBCryptolib.WBCRYPTO_gcm_context.ByReference gcm;
        public byte[] tag = new byte[16];
        public static class ByReference extends WBCryptolib.WBCRYPTO_gcmfile_context implements Structure.ByReference{}
        public static class ByValue extends WBCryptolib.WBCRYPTO_gcmfile_context implements Structure.ByValue{}
        @Override
        protected List<String> getFieldOrder(){
            return Arrays.asList("gcm", "tag");
        }
    }
    int WBCRYPTO_gcmfile_setiv(WBCryptolib.WBCRYPTO_gcmfile_context ctx, byte[] iv, int len);
    int WBCRYPTO_gcmfile_aad(WBCryptolib.WBCRYPTO_gcmfile_context ctx, byte[] aad, int len);
    int WBCRYPTO_gcmfile_encrypt(WBCryptolib.WBCRYPTO_gcmfile_context ctx,
                                 String infpath, String outfpath);
    int WBCRYPTO_gcmfile_decrypt(WBCryptolib.WBCRYPTO_gcmfile_context ctx,
                                 String infpath, String outfpath);
    void WBCRYPTO_gcmfile_free(WBCryptolib.WBCRYPTO_gcmfile_context ctx);

    /*********************************************sm4**********************************************/
    public static class WBCRYPTO_sm4_context extends Structure{
        public WBCRYPTO_sm4_context(){}
        public int[] rk = new int[32];
        public static class ByReference extends WBCryptolib.WBCRYPTO_sm4_context implements Structure.ByReference{}
        public static class ByValue extends WBCryptolib.WBCRYPTO_sm4_context implements Structure.ByValue{}
        @Override
        protected List<String> getFieldOrder(){
            return Arrays.asList("rk");
        }
    }
    WBCryptolib.WBCRYPTO_sm4_context WBCRYPTO_sm4_context_init();
    void WBCRYPTO_sm4_context_free(WBCryptolib.WBCRYPTO_sm4_context ctx);
    int WBCRYPTO_sm4_init_key(WBCryptolib.WBCRYPTO_sm4_context ctx, byte[] key, int keylen);
    int WBCRYPTO_sm4_encrypt(byte[] input, byte[] output, WBCryptolib.WBCRYPTO_sm4_context ctx);
    int WBCRYPTO_sm4_decrypt(byte[] input, byte[] output, WBCryptolib.WBCRYPTO_sm4_context ctx);
    int WBCRYPTO_sm4_cbc_encrypt(byte[] in, int inlen,
                                 byte[] out, int max_olen, IntByReference use_olen,
                                 WBCryptolib.WBCRYPTO_sm4_context ctx,
                                 byte[] ivec);
    int WBCRYPTO_sm4_cbc_decrypt(byte[] in, int inlen,
                                 byte[] out, int max_olen, IntByReference use_olen,
                                 WBCryptolib.WBCRYPTO_sm4_context ctx,
                                 byte[] ivec);
    WBCryptolib.WBCRYPTO_gcm_context WBCRYPTO_sm4_gcm_init(WBCryptolib.WBCRYPTO_sm4_context key);
    WBCryptolib.WBCRYPTO_gcmfile_context WBCRYPTO_sm4_gcmfile_init(WBCryptolib.WBCRYPTO_sm4_context key);

    /*******************************************wbsm4*************************************************/
    public static class WBCRYPTO_wbsm4_context extends Structure{
        public WBCRYPTO_wbsm4_context(){}
        public int rounds;
        public int encmode;
        public IntByReference MM;
        public IntByReference CC;
        public IntByReference DD;
        public int[] SE = new int[4096];
        public int[] FE = new int[4096];
        public IntByReference Table;
        public static class ByReference extends WBCryptolib.WBCRYPTO_wbsm4_context implements Structure.ByReference{}
        public static class ByValue extends WBCryptolib.WBCRYPTO_wbsm4_context implements Structure.ByValue{}
        @Override
        protected List<String> getFieldOrder(){
            return Arrays.asList("rounds", "encmode", "MM", "CC", "DD", "SE", "FE", "Table");
        }
    }
    WBCryptolib.WBCRYPTO_wbsm4_context WBCRYPTO_wbsm4_context_init(int encmode, int dummyrounds);
    void WBCRYPTO_wbsm4_context_free(WBCryptolib.WBCRYPTO_wbsm4_context ctx);
    int WBCRYPTO_wbsm4_gen_table(WBCryptolib.WBCRYPTO_wbsm4_context ctx,
                                 byte[] key, int keylen);
    int WBCRYPTO_wbsm4_encrypt(byte[] input, byte[] output, WBCryptolib.WBCRYPTO_wbsm4_context ctx);
    int WBCRYPTO_wbsm4_decrypt(byte[] input, byte[] output, WBCryptolib.WBCRYPTO_wbsm4_context ctx);
    int WBCRYPTO_wbsm4_cbc_encrypt(byte[] in, int inlen,
                                   byte[] out, int max_olen, IntByReference use_olen,
                                   WBCryptolib.WBCRYPTO_wbsm4_context ctx,
                                   byte[] ivec);
    int WBCRYPTO_wbsm4_cbc_decrypt(byte[] in, int inlen,
                                   byte[] out, int max_olen, IntByReference use_olen,
                                   WBCryptolib.WBCRYPTO_wbsm4_context ctx,
                                   byte[] ivec);
    WBCryptolib.WBCRYPTO_gcm_context WBCRYPTO_wbsm4_gcm_init(WBCryptolib.WBCRYPTO_wbsm4_context key);
    WBCryptolib.WBCRYPTO_gcmfile_context WBCRYPTO_wbsm4_gcmfile_init(WBCryptolib.WBCRYPTO_wbsm4_context key);
    //key exchange
    int WBCRYPTO_wbsm4_key2bytes(WBCryptolib.WBCRYPTO_wbsm4_context ctx, PointerByReference kstream);
    int WBCRYPTO_wbsm4_bytes2key(WBCryptolib.WBCRYPTO_wbsm4_context ctx, Pointer kstream);
    int WBCRYPTO_wbsm4_key2file(WBCryptolib.WBCRYPTO_wbsm4_context ctx, String fpath);
    int WBCRYPTO_wbsm4_file2key(WBCryptolib.WBCRYPTO_wbsm4_context ctx, String fpath);
}
