package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.models.WhiboxKey;
import com.scnu.whiboxkey.pksys.service.WhiboxKeyService;
import com.scnu.whiboxkey.pksys.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/whibox/manage")
public class WhiboxKeyController {
    @Autowired
    private WhiboxKeyService whiboxKeyService;

    @GetMapping("/key/list")
    public JSONResult findAllOfClientKey() {
        List<WhiboxKey> whiboxKeyList = whiboxKeyService.findAll();
        return JSONResult.ok(whiboxKeyList);
    }

    @GetMapping("/key/{id}")
    public JSONResult findOneOfClientKey(@PathVariable("id") Long id) {
        WhiboxKey whiboxKey = whiboxKeyService.findById(id);
        return JSONResult.ok(whiboxKey);
    }

    @PostMapping("/key")
    public JSONResult createClientKey(@RequestBody WhiboxKey whiboxKey) {
        WhiboxKey afWhiboxKey = whiboxKeyService.save(whiboxKey);
        return JSONResult.ok(afWhiboxKey);
    }

    @PutMapping("/key/{id}")
    public JSONResult updateClientKey(@PathVariable("id") Long id,
                                      @RequestBody WhiboxKey whiboxKey) {
        WhiboxKey afWhiboxKey = whiboxKeyService.update(id, whiboxKey);
        return JSONResult.ok(afWhiboxKey);
    }

    @DeleteMapping("/key/{id}")
    public JSONResult deleteClientKey(@PathVariable("id") Long id) {
        whiboxKeyService.deleteById(id);
        return JSONResult.ok("删除成功");
    }

}
