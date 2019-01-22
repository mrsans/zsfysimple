package com.thunisoft.zsfy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thunisoft.zsfy.bean.WxAccount;
import com.thunisoft.zsfy.constant.Constants;
import com.thunisoft.zsfy.constant.RedisKeys;
import com.thunisoft.zsfy.constant.TimeExpiration;
import com.thunisoft.zsfy.service.IWxAccount;
import com.thunisoft.zsfy.utils.JWTUtils;
import com.thunisoft.zsfy.utils.UUIDHelper;
import org.apache.commons.lang.StringUtils;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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
        final String r_token = String.format("%s_%s", RedisKeys.R_TOKEN, tokenAuth);
        stringRedisTemplate.opsForValue().set(r_token, jwt, TimeExpiration.THREE_MONTHS, TimeUnit.SECONDS);
        return "success";
    }

    @GetMapping("/login")
    public String login(HttpServletResponse response) {
        final String uuidNoDivider = UUIDHelper.getUUIDNoDivider();
        Cookie cookie = new Cookie(Constants.CookiesKey.TOKEN_AUTH, uuidNoDivider.toUpperCase());
        cookie.setHttpOnly(true);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.addCookie(cookie);
        return "/login";
    }

    @GetMapping("/logout")
    @ResponseBody
    public String logout (HttpServletRequest request, HttpServletResponse response,
                          @CookieValue(Constants.CookiesKey.TOKEN_AUTH) String tokenAuth) {
        // 清除cookie
        final Cookie[] cookies = request.getCookies();
        Arrays.stream(cookies).filter(cookie ->
            StringUtils.equals(cookie.getName(), Constants.CookiesKey.TOKEN_AUTH)
        ).forEach(cookie -> {
            cookie.setValue(null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        });
        // 清除redis
        final String r_token = String.format("%s_%s", RedisKeys.R_TOKEN, tokenAuth);
        stringRedisTemplate.expire(r_token, TimeExpiration.ZERO_SECOND, TimeUnit.MILLISECONDS);

        return "login success";
    }
}
