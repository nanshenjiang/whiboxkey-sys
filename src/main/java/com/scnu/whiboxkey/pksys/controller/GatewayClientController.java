package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.models.GatewayClient;
import com.scnu.whiboxkey.pksys.models.KeyMsg;
import com.scnu.whiboxkey.pksys.service.GatewayClientService;
import com.scnu.whiboxkey.pksys.service.KeyMsgService;
import com.scnu.whiboxkey.pksys.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/whibox/manage")
public class GatewayClientController {
    @Autowired
    private GatewayClientService gatewayClientService;

    @Autowired
    private KeyMsgService keyMsgService;

    @GetMapping("/client/list")
    public JSONResult findAllOfClientKey() {
        List<GatewayClient> gatewayClientList = gatewayClientService.findAll();
        return JSONResult.ok(gatewayClientList);
    }

    @GetMapping("/client/{id}")
    public JSONResult findOneOfClientKey(@PathVariable("id") Long id) {
        GatewayClient gatewayClient = gatewayClientService.findById(id);
        return JSONResult.ok(gatewayClient);
    }

    @PostMapping("/client")
    public JSONResult createClientKey(@RequestBody GatewayClient gatewayClient) {
        GatewayClient afgatewayClient = gatewayClientService.save(gatewayClient);
        return JSONResult.ok(afgatewayClient);
    }

    @PutMapping("/client/{id}")
    public JSONResult updateClientKey(@PathVariable("id") Long id,
                                         @RequestBody GatewayClient gatewayClient) {
        GatewayClient afgatewayClient = gatewayClientService.update(id, gatewayClient);
        return JSONResult.ok(afgatewayClient);
    }

    @PutMapping("/client/relation/key/{cid}/{kid}")
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

    @DeleteMapping("/client/{id}")
    public JSONResult deleteClientKey(@PathVariable("id") Long id) {
        gatewayClientService.deleteById(id);
        return JSONResult.ok("删除成功");
    }
}
