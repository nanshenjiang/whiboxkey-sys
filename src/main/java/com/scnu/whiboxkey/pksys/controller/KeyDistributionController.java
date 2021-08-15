package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.crypto.*;
import com.scnu.whiboxkey.pksys.models.CommunicationMsg;
import com.scnu.whiboxkey.pksys.models.GatewayClient;
import com.scnu.whiboxkey.pksys.models.GatewayServer;
import com.scnu.whiboxkey.pksys.models.WhiboxKey;
import com.scnu.whiboxkey.pksys.service.GatewayClientService;
import com.scnu.whiboxkey.pksys.service.GatewayServerService;
import com.scnu.whiboxkey.pksys.service.WhiboxKeyService;
import com.scnu.whiboxkey.pksys.utils.ByteUtil;
import com.scnu.whiboxkey.pksys.utils.JSONResult;
import com.scnu.whiboxkey.pksys.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/whibox/key")
public class KeyDistributionController {
    @Autowired
    private GatewayServerService serverKeyService;

    @Autowired
    private GatewayClientService clientKeyService;

    @Autowired
    private WhiboxKeyService whiboxKeyService;

    /**
     * 密钥过期，重新生成随机密钥，并更新数据库
     * @param whiboxKey 结构体
     */
    private void genRandKeyAndUpdateDB(WhiboxKey whiboxKey) throws FileNotFoundException {
        //生成随机密钥
        String key = RandomUtils.randomString(16);
        String encfpath = "";
        String decfpath = "";
        WBCryptolib.WBCRYPTO_wbsm4_context enc_ctx;
        WBCryptolib.WBCRYPTO_wbsm4_context dec_ctx;
        //选择算法
        if(whiboxKey.getWhiboxAlgName().equals("Dummyround-WBSM4")) {
            //算法为dummyround-SM4
            //生成白盒加密表
            enc_ctx = WhiboxKeyUtils.genRandomKeyOfWBSM4(key, 1);
            encfpath = WhiboxKeyUtils.keyToFileOfWBSM4(enc_ctx);
            //生成白盒解密表
            dec_ctx = WhiboxKeyUtils.genRandomKeyOfWBSM4(key, 0);
            decfpath = WhiboxKeyUtils.keyToFileOfWBSM4(dec_ctx);
        }
        if(encfpath.isEmpty()||decfpath.isEmpty()){
            throw new FileNotFoundException();
        }
        //生成新通行证
        String pass = RandomUtils.randomString(5)+System.currentTimeMillis();
        //计算过期时间
        Calendar cal = Calendar.getInstance();
        Date currentTime = new Date(System.currentTimeMillis());
        cal.setTime(currentTime);
        cal.add(Calendar.DATE, whiboxKey.getDuration());
        String encKey = SM4EncKey.KeystoreSM4EncKey(key);
        System.out.println("密钥为："+key);
        System.out.println("加密后密钥为："+encKey);
        //存储进数据库
        whiboxKey.setBlackKey(encKey);
        whiboxKey.setEncKfpath(encfpath);
        whiboxKey.setDecKfpath(decfpath);
        whiboxKey.setEffectiveTime(cal.getTime());
        whiboxKey.setPass(pass);
        whiboxKeyService.update(whiboxKey.getId(), whiboxKey);
    }

    /**
     * 判断密钥是否存在或有效，失效且符合要求将重新生成密钥
     * @param whiboxKey 白盒密钥结构体
     * @return 结果
     */
    private Map judgeKeyLegalAndGenKey(WhiboxKey whiboxKey) throws FileNotFoundException {
        Map ret=new HashMap<>();
        if (whiboxKey.getEncKfpath() != null) {
            //白盒密钥表存在
            Date currentTime = new Date(System.currentTimeMillis());
            if (currentTime.compareTo(whiboxKey.getEffectiveTime()) > 0) {
                //密钥过期，更新密钥
                String oldKfpath = whiboxKey.getEncKfpath();
                File oldkfile = new File(oldKfpath);
                if (oldkfile.exists()) {
                    oldkfile.delete();
                }
                oldKfpath = whiboxKey.getDecKfpath();
                oldkfile = new File(oldKfpath);
                if (oldkfile.exists()) {
                    oldkfile.delete();
                }
                genRandKeyAndUpdateDB(whiboxKey);
                ret.put("update", true);
                ret.put("pass", whiboxKey.getPass());
            } else {
                //密钥未过期
                ret.put("update", false);
                ret.put("pass", whiboxKey.getPass());
            }
        } else {
            //白盒密钥表不存在
            genRandKeyAndUpdateDB(whiboxKey);
            ret.put("update", true);
            ret.put("pass", whiboxKey.getPass());
        }
        return ret;
    }

    @PostMapping("/server/crypto/{sserial}/{cserial}")
    public JSONResult serverProcessData(@PathVariable("sserial") String sserial,
                                        @PathVariable("cserial") String cserial,
                                        @RequestBody CommunicationMsg communicationMsg) throws FileNotFoundException {
        if(!ByteUtil.isHexStr(communicationMsg.getIv())||!ByteUtil.isHexStr(communicationMsg.getText())){
            return JSONResult.error(400,"输入的消息或iv值需转换为16进制!");
        }
        Map ret=new HashMap<>();
        //查询服务端
        GatewayServer gatewayServer = serverKeyService.findBySerial(sserial);
        if(gatewayServer == null){
            return JSONResult.error(401, "未找到匹配的服务端");
        }
        if(!gatewayServer.getVaild()) {
            return JSONResult.error(401, "服务端授权失效");
        }
        //查询客户端
        GatewayClient gatewayClient = null;
        Collection<GatewayClient> ckCollection = gatewayServer.getClientKeyList();
        for(GatewayClient it: ckCollection){
            if(it.getSerial().equals(cserial)) {
                gatewayClient = it;
                break;
            }
        }
        if(gatewayClient == null){
            return JSONResult.error(401, "未找到匹配的客户端");
        }
        if(!gatewayClient.getVaild()) {
            return JSONResult.error(401, "客户端授权失效");
        }
        //算法待扩展
        if(!communicationMsg.getAlgorithm().equals("Dummyround-WBSM4")){
            return JSONResult.error(400, "目前仅支持算法：Dummyround-WBSM4");
        }
        //查询对应密钥
        WhiboxKey whiboxKey=null;
        Collection<WhiboxKey> whiboxKeyCollection = gatewayClient.getWhiboxKeyList();
        for(WhiboxKey it: whiboxKeyCollection) {
            if (it.getUpOrDown() == communicationMsg.getUpdown().equals("up")) {
                whiboxKey = it;
                break;
            }
            else if(it.getUpOrDown() == communicationMsg.getUpdown().equals("down")) {
                whiboxKey = it;
                break;
            }
        }
        if(whiboxKey == null)
            return JSONResult.error(400, "请注意：上下行密钥参数为up或down！");
        judgeKeyLegalAndGenKey(whiboxKey);
        //获取当前黑盒密钥
        String key=SM4EncKey.KeystoreSM4DecKey(whiboxKey.getBlackKey());
        //cbc模式
        if(communicationMsg.getMode().equals("cbc")){
            Sm4EncCBC sm4EncCBC=new Sm4EncCBC(key, communicationMsg.getIv(), communicationMsg.getText());
            if(communicationMsg.getEnc().equals("enc")){
                sm4EncCBC.sm4EncCbcFun();
            }else if (communicationMsg.getEnc().equals("dec")){
                sm4EncCBC.sm4DecCbcFun();
            }else {
                return JSONResult.error(400, "请注意：加解密参数为enc或dec");
            }
            ret.put("answer", sm4EncCBC.getAns());
        }else if (communicationMsg.getMode().equals("gcm")){
            Sm4EncGCM sm4EncGCM;
            if (communicationMsg.getAad()!=null)
                sm4EncGCM=new Sm4EncGCM(key, communicationMsg.getIv(), communicationMsg.getText());
            else
                sm4EncGCM=new Sm4EncGCM(key, communicationMsg.getIv(), communicationMsg.getAad(), communicationMsg.getText());
            if(communicationMsg.getEnc().equals("enc")){
                sm4EncGCM.sm4EncGcmFun();
            }else if (communicationMsg.getEnc().equals("dec")){
                sm4EncGCM.sm4DecGcmFun();
            }else {
                return JSONResult.error(400, "请注意：加解密参数为enc或dec");
            }
            ret.put("answer", sm4EncGCM.getAns());
            ret.put("tag", sm4EncGCM.getTag());
        }else {
            return JSONResult.error(400, "请注意：加密模式参数为cbc或gcm");
        }
        return JSONResult.ok(ret);
    }

    @GetMapping("/client/{serial}/{upOrDown}")
    public JSONResult clientAttachKey(@PathVariable("serial") String serial,
                                      @PathVariable("upOrDown") String upOrDown) throws FileNotFoundException {
        Map ret;
        GatewayClient gatewayClient = clientKeyService.findBySerial(serial);
        if(!gatewayClient.getVaild()) {
            return JSONResult.error(401, "客户端授权失效");
        }
        WhiboxKey whiboxKey=null;
        Collection<WhiboxKey> whiboxKeyCollection = gatewayClient.getWhiboxKeyList();
        for(WhiboxKey it: whiboxKeyCollection) {
            if (it.getUpOrDown() == upOrDown.equals("up")) {
                whiboxKey = it;
                break;
            }
            else if(it.getUpOrDown() == upOrDown.equals("down")) {
                whiboxKey = it;
                break;
            }
        }
        if(whiboxKey == null)
            return JSONResult.error(400, "请注意：上下行密钥参数为up或down！");
        ret = judgeKeyLegalAndGenKey(whiboxKey);
        return JSONResult.ok(ret);
    }

    @GetMapping("/download/{mode}/{pass}")
    public JSONResult keyFileDownLoad(HttpServletResponse response,
                               @PathVariable("mode") String mode,
                               @PathVariable("pass") String pass){
        //根据密钥通行证找到相关密钥进行下载操作
        WhiboxKey whiboxKey = whiboxKeyService.findByPass(pass);
        File file;
        if(mode.equals("enc")) {
            file = new File(whiboxKey.getEncKfpath());
        }else if(mode.equals("dec")){
            file = new File(whiboxKey.getDecKfpath());
        }else{
            return JSONResult.error(400, "请注意：加解密参数为enc或dec！");
        }
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
