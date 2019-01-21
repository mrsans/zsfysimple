package com.thunisoft.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thunisoft.bean.WxAccount;
import com.thunisoft.service.IWxAccount;
import com.thunisoft.utils.JWTUtils;
import com.thunisoft.zsfy.utils.UUIDHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController {

    private final String TOEKN_AUTH = "token_auth";

    private final long REDIS_EX = TimeUnit.HOURS.toSeconds(1);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IWxAccount wxAccount;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/user")
    @ResponseBody
    public Object login(@RequestParam("username")String username, @RequestParam("password")String password,
                        HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        final String uuidNoDivider = UUIDHelper.getUUIDNoDivider(0);
        final WxAccount wxAccount = this.wxAccount.login(username);
        Cookie cookie = new Cookie(TOEKN_AUTH, uuidNoDivider);
        response.addCookie(cookie);
        cookie.setMaxAge(24*60*60);
        final String jsonWx = objectMapper.writeValueAsString(wxAccount);
        final String jwt = JWTUtils.createJWT(jsonWx);
        stringRedisTemplate.opsForValue().set(uuidNoDivider, jwt, REDIS_EX);
        return "success";
    }

    @GetMapping("/login")
    public String login(HttpServletResponse response) {
        final String uuidNoDivider = UUIDHelper.getUUIDNoDivider(0);
        response.setHeader("cookie", uuidNoDivider);
        return "/login";
    }

    @GetMapping("/index")
    @ResponseBody
    public String index() {
        return "index";
    }

}
