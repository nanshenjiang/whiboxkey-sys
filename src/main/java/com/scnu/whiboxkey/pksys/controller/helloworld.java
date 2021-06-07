package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.utils.JSONResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/whibox")
public class helloworld {

    @GetMapping("/hello")
    public JSONResult helloworld() {
        return JSONResult.ok("helloworld");
    }
}
