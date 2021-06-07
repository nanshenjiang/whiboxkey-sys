package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.models.ClientKey;
import com.scnu.whiboxkey.pksys.service.ClientKeyService;
import com.scnu.whiboxkey.pksys.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/whibox/manage")
public class ClientKeyController {
    @Autowired
    private ClientKeyService clientKeyService;

    @GetMapping("/client/list")
    public JSONResult findAllOfClientKey() {
        List<ClientKey> clientKeyList = clientKeyService.findAll();
        return JSONResult.ok(clientKeyList);
    }

    @GetMapping("/client/{id}")
    public JSONResult findOneOfClientKey(@PathVariable("id") Long id) {
        ClientKey clientKey = clientKeyService.findById(id);
        return JSONResult.ok(clientKey);
    }

    @PostMapping("/client")
    public JSONResult createClientKey(@RequestBody ClientKey clientKey) {
        ClientKey afclientKey = clientKeyService.saveClientKey(clientKey);
        return JSONResult.ok(afclientKey);
    }

    @PutMapping("/client/{id}")
    public JSONResult updateClientKey(@PathVariable("id") Long id,
                                         @RequestBody ClientKey clientKey) {
        ClientKey afclientKey = clientKeyService.updateClientKey(id, clientKey);
        return JSONResult.ok(afclientKey);
    }

    @DeleteMapping("/client/{id}")
    public JSONResult deleteClientKey(@PathVariable("id") Long id) {
        clientKeyService.deleteById(id);
        return JSONResult.ok("删除成功");
    }
}
