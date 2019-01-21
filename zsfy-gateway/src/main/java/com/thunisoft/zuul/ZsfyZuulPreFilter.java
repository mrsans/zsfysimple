package com.thunisoft.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.thunisoft.zsfy.constant.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Zuul的过滤器
 */
@Component
public class ZsfyZuulPreFilter extends ZuulFilter{

    /**
     * 不需要认证的路径
     */
    private final String[] NO_FILTER = {"/login", "/login/**"};

    /**
     * 未认证 错误码403
     */
    private final String FORBIDDEN_TIP = "NO_FORBIDDEN";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        final HttpServletRequest request = requestContext.getRequest();
        final String authToken = request.getHeader(Constants.CookiesKey.TOKEN_AUTH);
        final String userInfo = StringUtils.isNotBlank(authToken) ?
                stringRedisTemplate.opsForValue().get(authToken): null;
        if (StringUtils.isBlank(userInfo)) {
            requestContext.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
            requestContext.setResponseBody(FORBIDDEN_TIP);
            requestContext.setSendZuulResponse(false);
        }
        return null;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        final HttpServletRequest request = requestContext.getRequest();
        return !PatternMatchUtils.simpleMatch(NO_FILTER, request.getRequestURI());
    }

}
