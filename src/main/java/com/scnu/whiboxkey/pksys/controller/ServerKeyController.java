package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.models.ClientKey;
import com.scnu.whiboxkey.pksys.models.ServerKey;
import com.scnu.whiboxkey.pksys.service.ClientKeyService;
import com.scnu.whiboxkey.pksys.service.ServerKeyService;
import com.scnu.whiboxkey.pksys.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/whibox/manage")
public class ServerKeyController {
    @Autowired
    private ServerKeyService serverKeyService;

    @Autowired
    private ClientKeyService clientKeyService;

    @GetMapping("/server/list")
    public JSONResult findAllOfServerKey() {
        List<ServerKey> serverKeyList = serverKeyService.findAll();
        return JSONResult.ok(serverKeyList);
    }

    @GetMapping("/server/{id}")
    public JSONResult findOneOfServerKey(@PathVariable("id") Long id) {
        ServerKey serverKey = serverKeyService.findById(id);
        return JSONResult.ok(serverKey);
    }

    @PostMapping("/server")
    public JSONResult createServerKey(@RequestBody ServerKey serverKey) {
        ServerKey afserverKey = serverKeyService.saveServerKey(serverKey);
        return JSONResult.ok(afserverKey);
    }

    @PutMapping("/server/{id}")
    public JSONResult updateServerKey(@PathVariable("id") Long id,
                                      @RequestBody ServerKey serverKey) {
        ServerKey afserverKey = serverKeyService.updateServerKey(id, serverKey);
        return JSONResult.ok(afserverKey);
    }

    @PutMapping("/server/relation/client/{sid}/{cid}")
    public JSONResult buildRelationBTServerAndClient(@PathVariable("sid") Long sid,
                                                     @PathVariable("cid") Long cid) {
        ClientKey ck = clientKeyService.findById(cid);
        ServerKey serverKey = serverKeyService.findById(sid);
        serverKey.getClientKeyList().add(ck);
        ServerKey afserverKey = serverKeyService.updateServerKey(sid, serverKey);
        return JSONResult.ok(afserverKey);
    }

    @DeleteMapping("/server/{id}")
    public JSONResult deleteServerKey(@PathVariable("id") Long id) {
        ServerKey sk = serverKeyService.findById(id);
        Collection<ClientKey> cks = sk.getClientKeyList();
        for(ClientKey ck : cks){
            clientKeyService.deleteById(ck.getId());
        }
        serverKeyService.deleteById(id);
        return JSONResult.ok("删除成功");
    }
}
