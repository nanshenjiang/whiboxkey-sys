package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.models.GCandKM;
import com.scnu.whiboxkey.pksys.models.GatewayClient;
import com.scnu.whiboxkey.pksys.models.GatewayServer;
import com.scnu.whiboxkey.pksys.models.KeyMsg;
import com.scnu.whiboxkey.pksys.service.GatewayClientService;
import com.scnu.whiboxkey.pksys.service.GatewayServerService;
import com.scnu.whiboxkey.pksys.service.KeyMsgService;
import com.scnu.whiboxkey.pksys.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/whibox/manage/client")
@CrossOrigin
public class GatewayClientController {
    @Autowired
    private GatewayServerService gatewayServerService;

    @Autowired
    private GatewayClientService gatewayClientService;

    @Autowired
    private KeyMsgService keyMsgService;

    @GetMapping("/query")
    public JSONResult findServerKeyQuery(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size){
        Page<GatewayClient> ret = gatewayClientService.findGCNoCriteria(page, size);
        return JSONResult.ok(ret.getContent());
    }

    @GetMapping("/list")
    public JSONResult findAllOfClientKey() {
        List<GatewayClient> gatewayClientList = gatewayClientService.findAll();
        return JSONResult.ok(gatewayClientList);
    }

    @GetMapping("/{id}")
    public JSONResult findOneOfClientKey(@PathVariable("id") Long id) {
        GatewayClient gatewayClient = gatewayClientService.findById(id);
        return JSONResult.ok(gatewayClient);
    }

    @PostMapping
    public JSONResult createClientKey(@RequestBody GatewayClient gatewayClient) {
        GatewayClient afgatewayClient = gatewayClientService.save(gatewayClient);
        return JSONResult.ok(afgatewayClient);
    }

    @PostMapping("/combination")
    public JSONResult createClientKeyWithCombination(@RequestBody GCandKM gcandkm) {
        //新建客户端
        GatewayClient gatewayClient = new GatewayClient(gcandkm.getSerial(),gcandkm.getVaild());
        GatewayClient afgatewayClient = gatewayClientService.save(gatewayClient);
        //建立客户端和服务端连接
        GatewayServer gs = gatewayServerService.findById(gcandkm.getSid());
        gs.getClientKeyList().add(afgatewayClient);
        GatewayServer afgatewayServer = gatewayServerService.update(gcandkm.getSid(), gs);
        //新建上行密钥和下行密钥
        KeyMsg upKeyMsg = new KeyMsg(true,gcandkm.getUpalgname(),gcandkm.getUpduration());
        KeyMsg afUpKeyMsg = keyMsgService.save(upKeyMsg);
        KeyMsg downKeyMsg = new KeyMsg(false,gcandkm.getDownalgname(),gcandkm.getDownduration());
        KeyMsg afDownKeyMsg = keyMsgService.save(downKeyMsg);
        //关联密钥与客户端
        afgatewayClient.getWhiboxKeyList().add(afUpKeyMsg);
        afgatewayClient.getWhiboxKeyList().add(afDownKeyMsg);
        GatewayClient afafgatewayClient = gatewayClientService.update(afgatewayClient.getId(), afgatewayClient);
        return JSONResult.ok(afafgatewayClient);
    }

    @PutMapping("/{id}")
    public JSONResult updateClientKey(@PathVariable("id") Long id,
                                         @RequestBody GatewayClient gatewayClient) {
        GatewayClient afgatewayClient = gatewayClientService.update(id, gatewayClient);
        return JSONResult.ok(afgatewayClient);
    }

    @PutMapping("/relation/key/{cid}/{kid}")
    public JSONResult buildRelationBTServerAndClient(@PathVariable("cid") Long cid,
                                                     @PathVariable("kid") Long kid) {
        KeyMsg wk = keyMsgService.findById(kid);
        GatewayClient gatewayClient = gatewayClientService.findById(cid);
        Collection<KeyMsg> keyMsgCollection = gatewayClient.getWhiboxKeyList();
        for(KeyMsg it: keyMsgCollection) {
            if (it.getUpOrDown() == wk.getUpOrDown())
                return JSONResult.error(403,"仅能存在一个上行密钥或下行密钥");
        }
        gatewayClient.getWhiboxKeyList().add(wk);
        GatewayClient afgatewayClient = gatewayClientService.update(cid, gatewayClient);
        return JSONResult.ok(afgatewayClient);
    }

    @DeleteMapping("/{id}")
    public JSONResult deleteClientKey(@PathVariable("id") Long id) {
        gatewayClientService.deleteById(id);
        return JSONResult.ok("删除成功");
    }
}
