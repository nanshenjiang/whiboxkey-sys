package com.scnu.whiboxkey.pksys.utils;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

public class WhiboxKeyUtils {

    /**
     * 生成wbsm4结构体
     * @param key 加解密的密钥
     * @param enc 选择加密或解密，1为加密，0为解密
     */
    public static WBCryptolib.WBCRYPTO_wbsm4_context genRandomKeyOfWBSM4(String key, int enc){
        WBCryptolib.WBCRYPTO_wbsm4_context ret = WBCryptolib.INSTANCE.WBCRYPTO_wbsm4_context_init();
        byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
        WBCryptolib.INSTANCE.WBCRYPTO_wbsm4_gen_table_with_dummyrounds(ret, byteKey, byteKey.length, enc, 1);
        return ret;
    }

    /**
     * 根据白盒密钥表结构体转换为密钥文件并存储，将文件路径返回
     */
    public static String keyToFileOfWBSM4(WBCryptolib.WBCRYPTO_wbsm4_context ctx) throws FileNotFoundException {
        File basePath = new File(ResourceUtils.getURL("classpath:").getPath());
        if(!basePath.exists()) {
            basePath = new File("");
        }
        File whiboxkeyStorePath = new File(basePath.getAbsolutePath(),"whiboxkey");
        if(!whiboxkeyStorePath.exists()) {
            whiboxkeyStorePath.mkdirs();
        }
        String keyBasePath = whiboxkeyStorePath.getPath();
        File filePath = new File(keyBasePath);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        String kfname = RandomUtils.randomString(5)+System.currentTimeMillis()+".whibox";
        String kfpath = keyBasePath + File.separator + kfname;
        System.out.println(kfpath);
        WBCryptolib.INSTANCE.WBCRYPTO_wbsm4_key2file(ctx, kfpath);
        return kfpath;
    }
}
