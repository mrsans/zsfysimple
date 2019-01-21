package com.thunisoft.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thunisoft.bean.WxAccount;
import com.thunisoft.service.IWxAccount;
import com.thunisoft.zsfy.constant.Constants;
import com.thunisoft.zsfy.utils.JWTUtils;
import com.thunisoft.zsfy.utils.UUIDHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IWxAccount wxAccount;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/user")
    @ResponseBody
    public Object login(@RequestParam("username")String username, @RequestParam("password")String password,
                        @CookieValue(Constants.CookiesKey.TOKEN_AUTH) String tokenAuth) throws JsonProcessingException {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        final WxAccount wxAccount = this.wxAccount.login(username);
        final String jsonWx = objectMapper.writeValueAsString(wxAccount);
        final String jwt = JWTUtils.createJWT(jsonWx);
        stringRedisTemplate.opsForValue().set(tokenAuth, jwt, Constants.TimeExpiration.ONE_DAY);
        return "success";
    }

    @GetMapping("/login")
    public String login(HttpServletResponse response) {
        final String uuidNoDivider = UUIDHelper.getUUIDNoDivider(0);
        Cookie cookie = new Cookie(Constants.CookiesKey.TOKEN_AUTH, uuidNoDivider.toUpperCase());
        cookie.setHttpOnly(true);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.addCookie(cookie);
        return "/login";
    }


}
