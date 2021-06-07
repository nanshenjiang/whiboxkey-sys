package com.scnu.whiboxkey.pksys.utils;

import com.sun.jna.*;
import com.sun.jna.ptr.PointerByReference;

import java.util.Arrays;
import java.util.List;

public interface WBCryptolib extends Library {
    WBCryptolib INSTANCE = (WBCryptolib) Native.loadLibrary("wbcrypto", WBCryptolib.class);

    /***********************************************sm3***********************************************/
    int WBCRYPTO_sm3(byte[] msg, int msglen, byte[] digest);

    /**********************************************wbmatrix*******************************************/
    public static class M32 extends Structure{
        public M32(){}
        public int[] M = new int[32];
        public static class ByReference extends WBCRYPTO_sm4_context implements Structure.ByReference{}
        public static class ByValue extends WBCRYPTO_sm4_context implements Structure.ByValue{}
        @Override
        protected List<String> getFieldOrder(){
            return Arrays.asList("M");
        }
    }
    public static class V32 extends Structure{
        public V32(){}
        public int V;
        public static class ByReference extends WBCRYPTO_sm4_context implements Structure.ByReference{}
        public static class ByValue extends WBCRYPTO_sm4_context implements Structure.ByValue{}
        @Override
        protected List<String> getFieldOrder(){
            return Arrays.asList("V");
        }
    }
    public static class Aff32 extends Structure{
        public Aff32(){}
        public M32 Mat ;
        public V32 Vec ;
        public static class ByReference extends WBCRYPTO_sm4_context implements Structure.ByReference{}
        public static class ByValue extends WBCRYPTO_sm4_context implements Structure.ByValue{}
        @Override
        protected List<String> getFieldOrder(){
            return Arrays.asList("Mat", "Vec");
        }
    }

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
        public Block block;
        public static class ByReference extends WBCRYPTO_gcm_context implements Structure.ByReference{}
        public static class ByValue extends WBCRYPTO_gcm_context implements Structure.ByValue{}
        @Override
        protected List<String> getFieldOrder(){
            return Arrays.asList("len", "add_len", "HL", "HH", "base_ectr", "y", "buf", "key", "block");
        }
    }
    int WBCRYPTO_gcm_setiv(WBCRYPTO_gcm_context ctx, byte[] iv, int len);
    int WBCRYPTO_gcm_aad(WBCRYPTO_gcm_context ctx, byte[] aad, int len);
    int WBCRYPTO_gcm_encrypt(WBCRYPTO_gcm_context ctx,
                             byte[] in, int inlen,
                             byte[] out, int outlen);
    int WBCRYPTO_gcm_decrypt(WBCRYPTO_gcm_context ctx,
                             byte[] in, int inlen,
                             byte[] out, int outlen);
    int WBCRYPTO_gcm_finish(WBCRYPTO_gcm_context ctx, byte[] tag, int len);
    void WBCRYPTO_gcm_free(WBCRYPTO_gcm_context ctx);

    /*******************************************gcmfile context**********************************************/
    public static class WBCRYPTO_gcmfile_context extends Structure{
        public WBCRYPTO_gcmfile_context(){}
        public WBCRYPTO_gcm_context.ByReference gcm;
        public byte[] tag = new byte[16];
        public static class ByReference extends WBCRYPTO_gcmfile_context implements Structure.ByReference{}
        public static class ByValue extends WBCRYPTO_gcmfile_context implements Structure.ByValue{}
        @Override
        protected List<String> getFieldOrder(){
            return Arrays.asList("gcm", "tag");
        }
    }
    int WBCRYPTO_gcmfile_setiv(WBCRYPTO_gcmfile_context ctx, byte[] iv, int len);
    int WBCRYPTO_gcmfile_aad(WBCRYPTO_gcmfile_context ctx, byte[] aad, int len);
    int WBCRYPTO_gcmfile_encrypt(WBCRYPTO_gcmfile_context ctx,
                                 String infpath, String outfpath);
    int WBCRYPTO_gcmfile_decrypt(WBCRYPTO_gcmfile_context ctx,
                                 String infpath, String outfpath);
    void WBCRYPTO_gcmfile_free(WBCRYPTO_gcmfile_context ctx);

    /*********************************************sm4**********************************************/
    public static class WBCRYPTO_sm4_context extends Structure{
        public WBCRYPTO_sm4_context(){}
        public int[] rk = new int[3];
        public static class ByReference extends WBCRYPTO_sm4_context implements Structure.ByReference{}
        public static class ByValue extends WBCRYPTO_sm4_context implements Structure.ByValue{}
        @Override
        protected List<String> getFieldOrder(){
            return Arrays.asList("rk");
        }
    }
    WBCRYPTO_sm4_context WBCRYPTO_sm4_context_init();
    void WBCRYPTO_sm4_context_free(WBCRYPTO_sm4_context ctx);
    int WBCRYPTO_sm4_init_key(WBCRYPTO_sm4_context ctx, byte[] key, int keylen);
    int WBCRYPTO_sm4_encrypt(byte[] input, byte[] output, WBCRYPTO_sm4_context ctx);
    int WBCRYPTO_sm4_decrypt(byte[] input, byte[] output, WBCRYPTO_sm4_context ctx);
    int WBCRYPTO_sm4_cbc_encrypt(byte[] in, int inlen,
                                 byte[] out, int outlen,
                                 WBCRYPTO_sm4_context ctx,
                                 byte[] ivec);
    int WBCRYPTO_sm4_cbc_decrypt(byte[] in, int inlen,
                                 byte[] out, int outlen,
                                 WBCRYPTO_sm4_context ctx,
                                 byte[] ivec);
    WBCRYPTO_gcm_context WBCRYPTO_sm4_gcm_init(WBCRYPTO_sm4_context key);
    WBCRYPTO_gcmfile_context WBCRYPTO_sm4_gcmfile_init(WBCRYPTO_sm4_context key);

    /*******************************************wbsm4*************************************************/
    public static class WBCRYPTO_wbsm4_context extends Structure{
        public WBCRYPTO_wbsm4_context(){}
        public int rounds;
        public int[] ssbox_enc=new int[102400];
        public Aff32[] M = new Aff32[300];
        public Aff32[] C = new Aff32[100];
        public Aff32[] D = new Aff32[100];
        public Aff32[] SE = new Aff32[4];
        public Aff32[] FE = new Aff32[4];
        public static class ByReference extends WBCRYPTO_wbsm4_context implements Structure.ByReference{}
        public static class ByValue extends WBCRYPTO_wbsm4_context implements Structure.ByValue{}
        @Override
        protected List<String> getFieldOrder(){
            return Arrays.asList("rounds", "ssbox_enc", "M", "C", "D", "SE", "FE");
        }
    }
    WBCRYPTO_wbsm4_context WBCRYPTO_wbsm4_context_init();
    void WBCRYPTO_wbsm4_context_free(WBCRYPTO_wbsm4_context ctx);
    int WBCRYPTO_wbsm4_gen_table_with_dummyrounds(WBCRYPTO_wbsm4_context ctx,
                                                  byte[] key, int keylen,
                                                  int encmode, int dummyrounds);
    int WBCRYPTO_wbsm4_encrypt(byte[] input, byte[] output, WBCRYPTO_wbsm4_context ctx);
    int WBCRYPTO_wbsm4_decrypt(byte[] input, byte[] output, WBCRYPTO_wbsm4_context ctx);
    int WBCRYPTO_wbsm4_cbc_encrypt(byte[] in, int inlen,
                                 byte[] out, int outlen,
                                 WBCRYPTO_wbsm4_context ctx,
                                 byte[] ivec);
    int WBCRYPTO_wbsm4_cbc_decrypt(byte[] in, int inlen,
                                 byte[] out, int outlen,
                                 WBCRYPTO_wbsm4_context ctx,
                                 byte[] ivec);
    WBCRYPTO_gcm_context WBCRYPTO_wbsm4_gcm_init(WBCRYPTO_wbsm4_context key);
    WBCRYPTO_gcmfile_context WBCRYPTO_wbsm4_gcmfile_init(WBCRYPTO_wbsm4_context key);
    //key exchange
    int WBCRYPTO_wbsm4_key2bytes(WBCRYPTO_wbsm4_context ctx, PointerByReference kstream);
    int WBCRYPTO_wbsm4_bytes2key(WBCRYPTO_wbsm4_context ctx, Pointer kstream);
    int WBCRYPTO_wbsm4_key2file(WBCRYPTO_wbsm4_context ctx, String fpath);
    int WBCRYPTO_wbsm4_file2key(WBCRYPTO_wbsm4_context ctx, String fpath);
}
