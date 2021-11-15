package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.models.GatewayClient;
import com.scnu.whiboxkey.pksys.models.GatewayServer;
import com.scnu.whiboxkey.pksys.service.GatewayClientService;
import com.scnu.whiboxkey.pksys.service.GatewayServerService;
import com.scnu.whiboxkey.pksys.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/whibox/manage/server")
@CrossOrigin
public class GatewayServerController {
    @Autowired
    private GatewayServerService gatewayServerService;

    @Autowired
    private GatewayClientService gatewayClientService;

    @GetMapping("/query")
    public JSONResult findServerKeyQuery(@RequestParam(value = "index", defaultValue = "0") Integer index,
                                  @RequestParam(value = "size", defaultValue = "10") Integer size){
        Page<GatewayServer> gatewayServerPage = gatewayServerService.findGSNoCriteria(index, size);
        Map<String,Object> ret = new HashMap<>();
        ret.put("list", gatewayServerPage.getContent());
        ret.put("index", gatewayServerPage.getNumber());
        ret.put("size", gatewayServerPage.getSize());
        ret.put("total", gatewayServerPage.getTotalPages());
        return JSONResult.ok(ret);
    }

    @GetMapping("/list")
    public JSONResult findAllOfServerKey() {
        List<GatewayServer> gatewayServerList = gatewayServerService.findAll();
        return JSONResult.ok(gatewayServerList);
    }

    @GetMapping("/serial/list")
    public JSONResult findAllOfServerKeyAndSerial() {
        List<GatewayServer> gatewayServerList = gatewayServerService.findAll();
        List<Map<String,Object>> gsMapList = new ArrayList<>();
        for(GatewayServer gs:gatewayServerList){
            Map<String,Object> gsMap = new HashMap<>();
            gsMap.put("value", gs.getId());
            gsMap.put("label", gs.getSerial());
            gsMapList.add(gsMap);
        }
        return JSONResult.ok(gsMapList);
    }

    @GetMapping("/{id}")
    public JSONResult findOneOfServerKey(@PathVariable("id") Long id) {
        GatewayServer gatewayServer = gatewayServerService.findById(id);
        return JSONResult.ok(gatewayServer);
    }

    @PostMapping
    public JSONResult createServerKey(@RequestBody GatewayServer gatewayServer) {
        //新建服务端
        GatewayServer gs = new GatewayServer(gatewayServer.getSerial(), gatewayServer.getIp(), gatewayServer.getVaild());
        GatewayServer afGatewayServer = gatewayServerService.save(gs);
        return JSONResult.ok(afGatewayServer);
    }

    @PutMapping("/{id}")
    public JSONResult updateServerKey(@PathVariable("id") Long id,
                                      @RequestBody GatewayServer gatewayServer) {
        GatewayServer afgatewayServer = gatewayServerService.update(id, gatewayServer);
        return JSONResult.ok(afgatewayServer);
    }

    @PutMapping("/relation/client/{sid}/{cid}")
    public JSONResult buildRelationBTServerAndClient(@PathVariable("sid") Long sid,
                                                     @PathVariable("cid") Long cid) {
        GatewayClient gc = gatewayClientService.findById(cid);
        GatewayServer gatewayServer = gatewayServerService.findById(sid);
        gatewayServer.getClientKeyList().add(gc);
        GatewayServer afgatewayServer = gatewayServerService.update(sid, gatewayServer);
        return JSONResult.ok(afgatewayServer);
    }

    @DeleteMapping("/{id}")
    public JSONResult deleteServerKey(@PathVariable("id") Long id) {
        gatewayServerService .deleteById(id);
        return JSONResult.ok("删除成功");
    }
}
