package com.thunisoft.zuul;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.thunisoft.zsfy.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 先放着 看是否有必要
 */
//@Component
public class ZsfyZuulPostFilter extends ZuulFilter{

    private final String STATUS_CODE = "code";
    private final String TOKEN_AUTH = "token_auth";
    private final String USERNAME = "username";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        final HttpServletRequest request = requestContext.getRequest();

        InputStream stream = requestContext.getResponseDataStream();
        String body = "";
        try {
            body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            final JSONObject jsonObject = JSON.parseObject(body);
            final int code = Integer.valueOf(String.valueOf(jsonObject.get(STATUS_CODE)));
            if (code ==  HttpStatus.SC_UNAUTHORIZED) {
                Map<String, Object> params = new HashMap<>();
                final String header = request.getHeader(TOKEN_AUTH);
                final String jwt = stringRedisTemplate.opsForValue().get(header);
                final Claims claims = JWTUtils.parseJWT(jwt);
                final String subject = claims.getSubject();
                final JSONObject jsonUserInfo = JSON.parseObject(subject);
                params.put("username", "");
                params.put("password", "");
//                jsonUserInfo.get();
//                restTemplate.getForEntity(, String.class,params );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

}
