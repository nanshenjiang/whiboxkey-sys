package com.scnu.whiboxkey.pksys.controller;

import com.scnu.whiboxkey.pksys.utils.JSONResult;
import org.springframework.web.bind.annotation.*;

/**
 * 账号密码登录操作
 * 不完善版本，账号密码写死
 * 账号：admin
 * 密码：admin
 */
@RestController
@RequestMapping("/whibox")
@CrossOrigin
public class LoginController {

    @PostMapping("/login/admin")
    public JSONResult loginInByAdmin(@RequestParam("username") String username,
                                   @RequestParam("password") String password){
        if("admin".equals(username)&&"admin".equals(password)) {
            return JSONResult.ok("");
        }
        else{
            return JSONResult.error(401,"账号密码错误！");
        }
    }

    @PostMapping("/login/pass")
    public JSONResult loginInJustPass(@RequestParam("password") String password){
        if("admin".equals(password)) {
            return JSONResult.ok("");
        }
        else{
            return JSONResult.error(401,"密码错误！");
        }
    }
}
