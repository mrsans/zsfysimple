package com.thunisoft.controller;

import com.thunisoft.service.IWxAccount;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private IWxAccount wxAccount;

    @GetMapping("/user")
    @ResponseBody
    public Object login(@RequestParam("username")String username, @RequestParam("password")String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        return "success";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/index")
    @ResponseBody
    public String index() {
        return "index";
    }

}
