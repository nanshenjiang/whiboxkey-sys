package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.crypto.*;
import com.scnu.whiboxkey.pksys.models.*;
import com.scnu.whiboxkey.pksys.service.GatewayClientService;
import com.scnu.whiboxkey.pksys.service.GatewayServerService;
import com.scnu.whiboxkey.pksys.service.KeyMsgService;
import com.scnu.whiboxkey.pksys.service.WhiboxKeyService;
import com.scnu.whiboxkey.pksys.utils.ByteUtil;
import com.scnu.whiboxkey.pksys.utils.JSONResult;
import com.scnu.whiboxkey.pksys.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/whibox/key")
public class KeyDistributionController {
    @Autowired
    private GatewayServerService gatewayServerService;

    @Autowired
    private GatewayClientService gatewayClientService;

    @Autowired
    private KeyMsgService keyMsgService;

    @Autowired
    private WhiboxKeyService whiboxKeyService;

    /**
     * 密钥过期，重新生成随机密钥，并更新数据库
     */
    private WhiboxKey genRandKeyAndUpdateDB(String whiboxAlgName, Boolean upOrDown, Integer duration, Long version) throws FileNotFoundException {
        //生成随机密钥
        String key = RandomUtils.randomString(16);
        String fpath = "";
        WBCryptolib.WBCRYPTO_wbsm4_context wbsm4_context;
        //选择算法
        if(whiboxAlgName.equals("Dummyround-WBSM4")) {
            //算法为dummyround-SM4
            if(upOrDown){
                //上行密钥，生成白盒加密表
                wbsm4_context = WhiboxKeyUtils.genRandomKeyOfWBSM4(key, 1);
                fpath = WhiboxKeyUtils.keyToFileOfWBSM4(wbsm4_context);
            }else {
                //下行密钥，生成白盒解密表
                wbsm4_context = WhiboxKeyUtils.genRandomKeyOfWBSM4(key, 0);
                fpath = WhiboxKeyUtils.keyToFileOfWBSM4(wbsm4_context);
            }
        }
        if(fpath.isEmpty()){
            throw new FileNotFoundException();
        }
        //生成新通行证
        String pass = RandomUtils.randomString(5)+System.currentTimeMillis();
        //计算过期时间
        Calendar cal = Calendar.getInstance();
        Date currentTime = new Date(System.currentTimeMillis());
        cal.setTime(currentTime);
        cal.add(Calendar.DATE, duration);
        //对密钥进行加密操作
        String encKey = SM4EncKey.KeystoreSM4EncKey(key);
        //生成新的白盒密钥表
        WhiboxKey whiboxKey = new WhiboxKey(encKey, version, fpath, pass, cal.getTime());
        //存储进数据库
        return whiboxKeyService.save(whiboxKey);
    }

    /**
     * 判断密钥是否存在或有效，失效且符合要求将重新生成密钥
     * @param keyMsg 白盒密钥结构体
     * @return 结果
     */
    private WhiboxKey judgeKeyLegalAndGenKey(KeyMsg keyMsg) throws FileNotFoundException {
        WhiboxKey wk = keyMsgService.findLatestVersion(keyMsg.getId());
        if (wk != null) {
            //白盒密钥表存在
            Date currentTime = new Date(System.currentTimeMillis());
            if (currentTime.compareTo(wk.getEffectiveTime()) > 0) {
                //密钥过期，更新密钥
//                String oldKfpath = wk.getKeyFpath();
//                File oldkfile = new File(oldKfpath);
//                if (oldkfile.exists()) {
//                    oldkfile.delete();
//                }
                WhiboxKey storewk = genRandKeyAndUpdateDB(keyMsg.getWhiboxAlgName(),keyMsg.getUpOrDown(),keyMsg.getDuration(), wk.getVersion()+1);
                keyMsgService.updateByWhiboxKey(keyMsg.getId(), storewk);
                return storewk;
            } else {
                //密钥未过期
                return wk;
            }
        } else {
            //白盒密钥表不存在
            WhiboxKey storewk = genRandKeyAndUpdateDB(keyMsg.getWhiboxAlgName(),keyMsg.getUpOrDown(),keyMsg.getDuration(), 1L);
            keyMsgService.updateByWhiboxKey(keyMsg.getId(), storewk);
            return storewk;
        }
    }

    @PostMapping("/server/enc/{sserial}/{cserial}")
    public JSONResult serverEncData(@PathVariable("sserial") String sserial,
                                    @PathVariable("cserial") String cserial,
                                    @RequestBody CommunicationMsg communicationMsg) throws FileNotFoundException {
        if(!ByteUtil.isHexStr(communicationMsg.getText())){
            return JSONResult.error(400,"输入的消息或iv值需转换为16进制!");
        }
        Map ret=new HashMap<>();
        //查询服务端
        GatewayServer gatewayServer = gatewayServerService.findBySerial(sserial);
        if(gatewayServer == null){
            return JSONResult.error(401, "未找到匹配的服务端");
        }
        if(!gatewayServer.getVaild()) {
            return JSONResult.error(401, "服务端授权失效");
        }
        //查询客户端
        GatewayClient gatewayClient = gatewayServerService.findByClientSerial(gatewayServer.getId(), cserial);
        if(gatewayClient == null){
            return JSONResult.error(401, "未找到匹配的客户端");
        }
        if(!gatewayClient.getVaild()) {
            return JSONResult.error(401, "客户端授权失效");
        }
        //算法待扩展
        if(!communicationMsg.getAlgorithm().equals("WBSM4")){
            return JSONResult.error(400, "目前仅支持算法：WBSM4");
        }
        //查询对应密钥
        KeyMsg keyMsg = gatewayClientService.getDownKey(gatewayClient.getId());
        WhiboxKey whiboxKey = judgeKeyLegalAndGenKey(keyMsg);
        //获取当前黑盒密钥
        String key=SM4EncKey.KeystoreSM4DecKey(whiboxKey.getBlackKey());
        //生成随机iv值
        String iv = RandomUtils.random16Hex(32);
//        String iv = "000102030405060708090a0b0c0d0e0f";
        //进行加解密操作
        if(communicationMsg.getMode().equals("cbc")){
            //cbc模式
            Sm4EncCBC sm4EncCBC=new Sm4EncCBC(key, iv, communicationMsg.getText());
            sm4EncCBC.sm4EncCbcFun();
            ret.put("answer", sm4EncCBC.getAns());
        }else if (communicationMsg.getMode().equals("gcm")){
            //gcm模式
            Sm4EncGCM sm4EncGCM = new Sm4EncGCM(key, iv, communicationMsg.getText());
            sm4EncGCM.sm4EncGcmFun();
            ret.put("answer", sm4EncGCM.getAns());
            ret.put("tag", sm4EncGCM.getTag());
        }else {
            return JSONResult.error(400, "请注意：加密模式参数为cbc或gcm");
        }
        ret.put("iv", iv);
        ret.put("version", whiboxKey.getVersion());
        return JSONResult.ok(ret);
    }

    @PostMapping("/server/dec/{sserial}/{cserial}/{version}")
    public JSONResult serverDecData(@PathVariable("sserial") String sserial,
                                    @PathVariable("cserial") String cserial,
                                    @PathVariable("version") Long version,
                                    @RequestBody CommunicationMsg communicationMsg) {
        if(!ByteUtil.isHexStr(communicationMsg.getIv())||!ByteUtil.isHexStr(communicationMsg.getText())){
            return JSONResult.error(400,"输入的消息或iv值需转换为16进制!");
        }
        if(communicationMsg.getMode().equals("cbc")){
            if(communicationMsg.getIv().length()!=32){
                return JSONResult.error(400,"cbc模式的iv值需转换为16bytes长度的16进制数!");
            }
        }
        Map ret=new HashMap<>();
        //查询服务端
        GatewayServer gatewayServer = gatewayServerService.findBySerial(sserial);
        if(gatewayServer == null){
            return JSONResult.error(401, "未找到匹配的服务端");
        }
        if(!gatewayServer.getVaild()) {
            return JSONResult.error(401, "服务端授权失效");
        }
        //查询客户端
        GatewayClient gatewayClient = gatewayServerService.findByClientSerial(gatewayServer.getId(), cserial);
        if(gatewayClient == null){
            return JSONResult.error(401, "未找到匹配的客户端");
        }
        if(!gatewayClient.getVaild()) {
            return JSONResult.error(401, "客户端授权失效");
        }
        //算法待扩展
        if(!communicationMsg.getAlgorithm().equals("WBSM4")){
            return JSONResult.error(400, "目前仅支持算法：WBSM4");
        }
        //查询对应密钥
        KeyMsg keyMsg = gatewayClientService.getUpKey(gatewayClient.getId());
        WhiboxKey whiboxKey = keyMsgService.findByVersion(keyMsg.getId(), version);
        if(whiboxKey == null){
            return JSONResult.error(401, "版本错误，未查找到密钥！");
        }
        //获取当前黑盒密钥
        String key=SM4EncKey.KeystoreSM4DecKey(whiboxKey.getBlackKey());
        //进行加解密操作
        if(communicationMsg.getMode().equals("cbc")){
            //cbc模式
            Sm4EncCBC sm4EncCBC=new Sm4EncCBC(key, communicationMsg.getIv(), communicationMsg.getText());
            sm4EncCBC.sm4DecCbcFun();
            ret.put("answer", sm4EncCBC.getAns());
        }else if (communicationMsg.getMode().equals("gcm")){
            //gcm模式
            Sm4EncGCM sm4EncGCM = new Sm4EncGCM(key, communicationMsg.getIv(), communicationMsg.getText());
            sm4EncGCM.sm4DecGcmFun();
            ret.put("answer", sm4EncGCM.getAns());
            ret.put("tag", sm4EncGCM.getTag());
        }else {
            return JSONResult.error(400, "请注意：加密模式参数为cbc或gcm");
        }
        ret.put("version", whiboxKey.getVersion());
        return JSONResult.ok(ret);
    }

    @GetMapping("/client/up/{serial}")
    public JSONResult clientAttachUpKey(@PathVariable("serial") String serial) throws FileNotFoundException {
        Map ret=new HashMap<>();
        GatewayClient gatewayClient = gatewayClientService.findBySerial(serial);
        if(gatewayClient == null){
            return JSONResult.error(401, "未找到匹配的客户端");
        }
        if(!gatewayClient.getVaild()) {
            return JSONResult.error(401, "客户端授权失效");
        }
        KeyMsg keyMsg = gatewayClientService.getUpKey(gatewayClient.getId());
        WhiboxKey retWk = judgeKeyLegalAndGenKey(keyMsg);
        ret.put("pass", retWk.getPass());
        ret.put("version", retWk.getVersion());
        return JSONResult.ok(ret);
    }

    @GetMapping("/client/down/{serial}/{version}")
    public JSONResult clientAttachKey(@PathVariable("serial") String serial,
                                      @PathVariable("version") Long version) {
        Map ret=new HashMap<>();
        GatewayClient gatewayClient = gatewayClientService.findBySerial(serial);
        if(gatewayClient == null){
            return JSONResult.error(401, "未找到匹配的客户端");
        }
        if(!gatewayClient.getVaild()) {
            return JSONResult.error(401, "客户端授权失效");
        }
        KeyMsg keyMsg = gatewayClientService.getDownKey(gatewayClient.getId());
        WhiboxKey whiboxKey = keyMsgService.findByVersion(keyMsg.getId(), version);
        ret.put("pass", whiboxKey.getPass());
        ret.put("version", whiboxKey.getVersion());
        return JSONResult.ok(ret);
    }

    @GetMapping("/download/{pass}")
    public JSONResult keyFileDownLoad(HttpServletResponse response,
                               @PathVariable("pass") String pass){
        //根据密钥通行证找到相关密钥进行下载操作
        WhiboxKey whiboxKey = whiboxKeyService.findByPass(pass);
        File file = new File(whiboxKey.getKeyFpath());
        if(!file.exists()){
            return JSONResult.error(404, "密钥文件生成出错，无法下载");
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName() );

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            byte[] buff = new byte[1024];
            OutputStream os  = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            return JSONResult.error(404, "下载失败");
        }
        return null;
    }
}
