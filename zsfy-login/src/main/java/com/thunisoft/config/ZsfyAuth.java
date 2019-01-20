package com.thunisoft.config;

import com.thunisoft.utils.JWTUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class ZsfyAuth extends FormAuthenticationFilter {

    private final String TOKEN_AUTH = "token_auth";

    private final long REDIS_EX = TimeUnit.HOURS.toSeconds(1);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {
        //获取已登录的用户信息
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final String jwt = JWTUtils.createJWT("1");
        stringRedisTemplate.opsForValue().set("1", jwt, REDIS_EX);
        httpResponse.setHeader(TOKEN_AUTH, TOKEN_AUTH);
        return super.onLoginSuccess(token, subject, request, response);
    }

}
