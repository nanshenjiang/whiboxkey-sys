package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.models.ClientKey;
import com.scnu.whiboxkey.pksys.models.ServerKey;
import com.scnu.whiboxkey.pksys.service.ClientKeyService;
import com.scnu.whiboxkey.pksys.service.ServerKeyService;
import com.scnu.whiboxkey.pksys.utils.JSONResult;
import com.scnu.whiboxkey.pksys.utils.RandomUtils;
import com.scnu.whiboxkey.pksys.utils.WBCryptolib;
import com.scnu.whiboxkey.pksys.utils.WhiboxKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/whibox/key")
public class KeyDistributionController {
    @Autowired
    private ServerKeyService serverKeyService;

    @Autowired
    private ClientKeyService clientKeyService;

    //密钥过期，重新生成随机密钥，并更新数据库
    private void genRandKeyAndUpdateDB(ClientKey clientKey) throws FileNotFoundException {
        String key = RandomUtils.randomString(16);
        WBCryptolib.WBCRYPTO_wbsm4_context ctx= WhiboxKeyUtils.genRandomKeyOfWBSM4(key, 1);
        String fpath = WhiboxKeyUtils.keyToFileOfWBSM4(ctx);
        String pass = RandomUtils.randomString(5)+System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        Date currentTime = new Date(System.currentTimeMillis());
        cal.setTime(currentTime);
        cal.add(Calendar.DATE, clientKey.getDuration());
        clientKey.setEffectiveTime(cal.getTime());
        clientKey.setBkey(key);
        clientKey.setWkfpath(fpath);
        clientKey.setPass(pass);
        clientKeyService.updateClientKey(clientKey.getId(), clientKey);
    }

    //判断密钥是否存在或有效，失效且符合要求将重新生成密钥
    private Map judgeKeyExitAndGenKey(ClientKey clientKey) throws FileNotFoundException {
        Map ret=new HashMap<>();
        if(!clientKey.getVaild()) {
            return null;
        }
        if(clientKey.getWkfpath()!=null){
            //白盒密钥表存在
            Date currentTime = new Date(System.currentTimeMillis());
            if(currentTime.compareTo(clientKey.getEffectiveTime()) >0){
                //密钥过期，更新密钥
                String oldKfpath = clientKey.getWkfpath();
                File oldkfile = new File(oldKfpath);
                if(oldkfile.exists()){
                    oldkfile.delete();
                }
                genRandKeyAndUpdateDB(clientKey);
                ret.put("update",true);
                ret.put("pass", clientKey.getPass());
            }else{
                //密钥未过期
                ret.put("update",false);
                ret.put("pass", clientKey.getPass());
            }
        }else {
            //白盒密钥表不存在
            genRandKeyAndUpdateDB(clientKey);
            ret.put("update",true);
            ret.put("pass", clientKey.getPass());
        }
        return ret;
    }

    @GetMapping("/server/{sserial}/{cserial}")
    public JSONResult serverAttachKey(@PathVariable("sserial") String sserial,
                                      @PathVariable("cserial") String cserial) throws FileNotFoundException {
        Map<String, String> ret = new HashMap<>();
        ServerKey serverKey = serverKeyService.findBySerial(sserial);
        if(!serverKey.getVaild()){
            return JSONResult.error(401, "服务端授权过期或失效");
        }
        ClientKey clientKey = clientKeyService.findBySerial(cserial);
        Collection<ClientKey> keyList = serverKey.getClientKeyList();
        if(!keyList.contains(clientKey)){
            return JSONResult.error(404, "未找到匹配的客户端");
        }
        Map check = judgeKeyExitAndGenKey(clientKey);
        if(check == null)
            return JSONResult.error(401, "身份序列为"+clientKey.getSerial()+"的客户端授权失效");
        ret.put("key", clientKey.getBkey());
        return JSONResult.ok(ret);
    }

    @GetMapping("/server/{sserial}")
    public JSONResult serverAttachKeyList(@PathVariable("sserial") String sserial) throws FileNotFoundException {
        List<Map<String,String>> ret = new ArrayList<>();
        ServerKey serverKey = serverKeyService.findBySerial(sserial);
        if(!serverKey.getVaild()) {
            return JSONResult.error(401, "服务端授权过期或失效");
        }
        Collection<ClientKey> keyList = serverKey.getClientKeyList();
        for (ClientKey it : keyList) {
            judgeKeyExitAndGenKey(it);
            Map<String, String> one = new HashMap<>();
            one.put("ClientSerial", it.getSerial());
            one.put("key", it.getBkey());
            ret.add(one);
        }
        return JSONResult.ok(ret);
    }



    @GetMapping("/client/{serial}")
    public JSONResult clientAttachKey(@PathVariable("serial") String serial) throws FileNotFoundException {
        ClientKey clientKey = clientKeyService.findBySerial(serial);
        Map ret = judgeKeyExitAndGenKey(clientKey);
        if(ret == null)
            return JSONResult.error(401, "客户端授权失效");
        else
            return JSONResult.ok(ret);
    }

    @GetMapping("/download/{pass}")
    public JSONResult fileDownLoad(HttpServletResponse response,
                               @PathVariable("pass") String pass){
        //根据密钥通行证找到相关密钥进行下载操作
        ClientKey clientKey = clientKeyService.findByPass(pass);
        File file = new File(clientKey.getWkfpath());
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
        return JSONResult.ok("下载成功");

    }
}
