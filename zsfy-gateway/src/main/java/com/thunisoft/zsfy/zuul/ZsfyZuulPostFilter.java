package com.thunisoft.zsfy.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.thunisoft.zsfy.constant.Constants;
import com.thunisoft.zsfy.constant.RedisKeys;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Zuul的过滤器
 */
@Component
public class ZsfyZuulPostFilter extends ZuulFilter{

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
        final HttpServletResponse response = requestContext.getResponse();
        final HttpServletRequest request = requestContext.getRequest();
        final String uri = request.getRequestURI();
        final String url = request.getRequestURL().toString();
        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
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
        RequestContext requestContext = RequestContext.getCurrentContext();
        final HttpServletRequest request = requestContext.getRequest();
        return !PatternMatchUtils.simpleMatch(NO_FILTER, request.getRequestURI());
    }

}
