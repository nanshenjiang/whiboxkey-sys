package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.models.GatewayClient;
import com.scnu.whiboxkey.pksys.models.GatewayServer;
import com.scnu.whiboxkey.pksys.service.GatewayClientService;
import com.scnu.whiboxkey.pksys.service.GatewayServerService;
import com.scnu.whiboxkey.pksys.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/whibox/manage")
public class GatewayServerController {
    @Autowired
    private GatewayServerService gatewayServerService;

    @Autowired
    private GatewayClientService gatewayClientService;

    @GetMapping("/server/list")
    public JSONResult findAllOfServerKey() {
        List<GatewayServer> gatewayServerList = gatewayServerService.findAll();
        return JSONResult.ok(gatewayServerList);
    }

    @GetMapping("/server/{id}")
    public JSONResult findOneOfServerKey(@PathVariable("id") Long id) {
        GatewayServer gatewayServer = gatewayServerService.findById(id);
        return JSONResult.ok(gatewayServer);
    }

    @PostMapping("/server")
    public JSONResult createServerKey(@RequestBody GatewayServer gatewayServer) {
        GatewayServer afgatewayServer = gatewayServerService.save(gatewayServer);
        return JSONResult.ok(afgatewayServer);
    }

    @PutMapping("/server/{id}")
    public JSONResult updateServerKey(@PathVariable("id") Long id,
                                      @RequestBody GatewayServer gatewayServer) {
        GatewayServer afgatewayServer = gatewayServerService.update(id, gatewayServer);
        return JSONResult.ok(afgatewayServer);
    }

    @PutMapping("/server/relation/client/{sid}/{cid}")
    public JSONResult buildRelationBTServerAndClient(@PathVariable("sid") Long sid,
                                                     @PathVariable("cid") Long cid) {
        GatewayClient gc = gatewayClientService.findById(cid);
        GatewayServer gatewayServer = gatewayServerService.findById(sid);
        gatewayServer.getClientKeyList().add(gc);
        GatewayServer afgatewayServer = gatewayServerService.update(sid, gatewayServer);
        return JSONResult.ok(afgatewayServer);
    }

    @DeleteMapping("/server/{id}")
    public JSONResult deleteServerKey(@PathVariable("id") Long id) {
        gatewayServerService .deleteById(id);
        return JSONResult.ok("删除成功");
    }
}
