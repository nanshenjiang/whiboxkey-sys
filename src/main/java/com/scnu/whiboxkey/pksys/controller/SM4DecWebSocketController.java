package com.scnu.whiboxkey.pksys.controller;

import com.alibaba.fastjson.JSONObject;
import com.scnu.whiboxkey.pksys.crypto.*;
import com.scnu.whiboxkey.pksys.models.*;
import com.scnu.whiboxkey.pksys.service.GatewayClientService;
import com.scnu.whiboxkey.pksys.service.GatewayServerService;
import com.scnu.whiboxkey.pksys.service.KeyMsgService;
import com.scnu.whiboxkey.pksys.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;

class MsgTransfer{
    private String serial;

    private String password;

    private Long version;

    public MsgTransfer() {
    }

    public MsgTransfer(String serial, String password, Long version) {
        this.serial = serial;
        this.password = password;
        this.version = version;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}

class ComMsg {
    //算法名称，目前仅支持WBSM4
    private String algorithm;

    //是加密模式，CBC或GCM模式，参数为cbc或gcm
    private String mode;

    //初始值
    private String iv;

    //进行操作的消息
    private String text;

    //当前操作的顺序号
    private Long pid;

    public ComMsg() {
    }

    public ComMsg(String algorithm, String mode, String iv, String text, Long pid) {
        this.algorithm = algorithm;
        this.mode = mode;
        this.iv = iv;
        this.text = text;
        this.pid = pid;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}

/**
 * 使用WebSocket建立长连接
 */
@ServerEndpoint("/whibox/key/websocket/dec/{sserial}/{cserial}/{version}")
@Component
public class SM4DecWebSocketController {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineNum = new AtomicInteger();

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<MsgTransfer, Session> sessionPools = new ConcurrentHashMap<>();

    /**
     * 由于websocket是多对象，和spring的单例冲突，所以需要使用该方法初始化
     */
    private static GatewayServerService gatewayServerService;

    @Autowired
    public void setGatewayServerService(GatewayServerService gatewayServerService) {
        SM4DecWebSocketController.gatewayServerService = gatewayServerService;
    }

    private static GatewayClientService gatewayClientService;

    @Autowired
    public void setGatewayClientService(GatewayClientService gatewayClientService) {
        SM4DecWebSocketController.gatewayClientService = gatewayClientService;
    }

    private static KeyMsgService keyMsgService;

    @Autowired
    public void setKeyMsgService(KeyMsgService keyMsgService) {
        SM4DecWebSocketController.keyMsgService = keyMsgService;
    }

    //发送消息
    public void sendMessage(Session session, String message) throws IOException {
        if(session != null){
            synchronized (session) {
                session.getBasicRemote().sendText(message);
            }
        }
    }

    /**
     * 建立连接
     * 查询数据库，得到加解密所需的密钥
     * 新建客户端线程加入线程池
     */
    @OnOpen
    public void onOpen(Session session,
                       @PathParam("sserial") String sserial,
                       @PathParam("cserial") String cserial,
                       @PathParam("version") Long version){
        //查询服务端
        GatewayServer gatewayServer = gatewayServerService.findBySerial(sserial);
        if(gatewayServer == null){
            return;
        }
        if(!gatewayServer.getVaild()) {
            return;
        }
        //查询客户端
        GatewayClient gatewayClient = gatewayServerService.findByClientSerial(gatewayServer.getId(), cserial);
        if(gatewayClient == null){
            return;
        }
        if(!gatewayClient.getVaild()) {
            return;
        }
        //查询对应密钥
        KeyMsg keyMsg = gatewayClientService.getUpKey(gatewayClient.getId());
        WhiboxKey whiboxKey = keyMsgService.findByVersion(keyMsg.getId(), version);
        if(whiboxKey == null){
            return;
        }
        //获取当前黑盒密钥
        String key=SM4EncKey.KeystoreSM4DecKey(whiboxKey.getBlackKey());
        MsgTransfer msgTransfer = new MsgTransfer(sserial, key, whiboxKey.getVersion());
        sessionPools.put(msgTransfer, session);
        addOnlineCount();
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(@PathParam("sserial") String sserial){
        for (MsgTransfer msgTransfer: sessionPools.keySet()) {
            if(msgTransfer.getSerial().equals(sserial)){
                sessionPools.remove(msgTransfer);
                subOnlineCount();
            }
        }
    }

    /**
     * 接受客户端发来的消息，对消息进行处理
     * 对消息进行cbc解密或gcm解密
     */
    @OnMessage
    public void onMessage(@PathParam(value = "sserial") String sserial,
                          String message) throws IOException{
        for (MsgTransfer msgTransfer: sessionPools.keySet()) {
            try {
                if(msgTransfer.getSerial().equals(sserial)){
                    Session session = sessionPools.get(msgTransfer);
                    String sendmsg;
                    Map<String, Object> ret = new HashMap<>();
                    //将消息转换为json对象
                    ComMsg comMsg = JSONObject.parseObject(message, ComMsg.class);
                    if(!ByteUtil.isHexStr(comMsg.getIv())||!ByteUtil.isHexStr(comMsg.getText())){
                        ret.put("code",400);
                        ret.put("msg","输入的消息或iv值需转换为16进制!");
                        sendmsg = JSONObject.toJSONString(ret);
                        sendMessage(session, sendmsg);
                        break;
                    }
                    if("cbc".equals(comMsg.getMode())){
                        if(comMsg.getIv().length()!=32){
                            ret.put("code",400);
                            ret.put("msg","cbc模式的iv值需转换为16bytes长度的16进制数!");
                            sendmsg = JSONObject.toJSONString(ret);
                            sendMessage(session, sendmsg);
                            break;
                        }
                    }
                    //算法待扩展
                    if(!"WBSM4".equals(comMsg.getAlgorithm())){
                        ret.put("code",400);
                        ret.put("msg","目前仅支持算法：WBSM4");
                        sendmsg = JSONObject.toJSONString(ret);
                        sendMessage(session, sendmsg);
                        break;
                    }
                    //进行加解密操作
                    if("cbc".equals(comMsg.getMode())){
                        //cbc模式
                        Sm4EncCBC sm4EncCBC=new Sm4EncCBC(msgTransfer.getPassword(), comMsg.getIv(), comMsg.getText());
                        sm4EncCBC.sm4DecCbcFun();
                        ret.put("answer", sm4EncCBC.getAns());
                    }else if ("gcm".equals(comMsg.getMode())){
                        //gcm模式
                        Sm4EncGCM sm4EncGCM = new Sm4EncGCM(msgTransfer.getPassword(), comMsg.getIv(), comMsg.getText());
                        sm4EncGCM.sm4DecGcmFun();
                        ret.put("answer", sm4EncGCM.getAns());
                        ret.put("tag", sm4EncGCM.getTag());
                    }else {
                        ret.put("code",400);
                        ret.put("msg","请注意：加密模式参数为cbc或gcm");
                        sendmsg = JSONObject.toJSONString(ret);
                        sendMessage(session, sendmsg);
                        break;
                    }
                    ret.put("version", msgTransfer.getVersion());
                    ret.put("code", 200);
                    ret.put("pid", comMsg.getPid());
                    sendmsg = JSONObject.toJSONString(ret);
                    sendMessage(session, sendmsg);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable throwable){
        throwable.printStackTrace();
    }

    public static void addOnlineCount(){
        onlineNum.incrementAndGet();
    }

    public static void subOnlineCount() {
        onlineNum.decrementAndGet();
    }

}
