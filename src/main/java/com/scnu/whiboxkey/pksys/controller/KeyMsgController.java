package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.models.KeyMsg;
import com.scnu.whiboxkey.pksys.service.KeyMsgService;
import com.scnu.whiboxkey.pksys.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/whibox/manage")
public class KeyMsgController {
    @Autowired
    private KeyMsgService keyMsgService;

    @GetMapping("/key/list")
    public JSONResult findAllOfClientKey() {
        List<KeyMsg> keyMsgList = keyMsgService.findAll();
        return JSONResult.ok(keyMsgList);
    }

    @GetMapping("/key/{id}")
    public JSONResult findOneOfClientKey(@PathVariable("id") Long id) {
        KeyMsg keyMsg = keyMsgService.findById(id);
        return JSONResult.ok(keyMsg);
    }

    @PostMapping("/key")
    public JSONResult createClientKey(@RequestBody KeyMsg keyMsg) {
        KeyMsg afKeyMsg = keyMsgService.save(keyMsg);
        return JSONResult.ok(afKeyMsg);
    }

    @PutMapping("/key/{id}")
    public JSONResult updateClientKey(@PathVariable("id") Long id,
                                      @RequestBody KeyMsg keyMsg) {
        KeyMsg afKeyMsg = keyMsgService.update(id, keyMsg);
        return JSONResult.ok(afKeyMsg);
    }

    @DeleteMapping("/key/{id}")
    public JSONResult deleteClientKey(@PathVariable("id") Long id) {
        keyMsgService.deleteById(id);
        return JSONResult.ok("删除成功");
    }

}
