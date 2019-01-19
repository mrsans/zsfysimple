package com.thunisoft.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thunisoft.bean.WxAccount;
import com.thunisoft.service.IWxAccount;
import com.thunisoft.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author ZhPJ
 * @Date 2019/1/15 001512:04
 * @Version 1.0
 * @Description:
 */
public class ZsfyShiroRealm extends AuthorizingRealm {

    /**
     * 认证的TOKEN值
     */
    private final String AUTH_TOKEN = "auth_token";
    /**
     * redis的token前缀
     */
    private final String RKEY_TOKEN_PREFIX = "token_";


    @Autowired
    private IWxAccount wxAccountService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获得该用户角色
        String role = "111";
        Set<String> set = new HashSet<>();
        //需要将 role 封装到 Set 作为 info.setRoles() 的参数
        set.add(role);
        //设置该用户拥有的角色
        info.setRoles(set);
        return info;
    }

    /**
     * @deprecation: 用户认证
     * @param:
     * @return:
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1.前台获取request
        HttpServletRequest request = null;
        final String header = "";
        // 2.通过请求头获取对应的认证token
        if (StringUtils.isNotBlank(request.getHeader(AUTH_TOKEN))) {
            // 3 判断token是否过期
            final String rkey = String.format("{0}_{1}", this.RKEY_TOKEN_PREFIX, header);
            final String rvalue = stringRedisTemplate.opsForValue().get(rkey);
            if (StringUtils.isNotBlank(rvalue)) {
                // 3.0 解析rvalue，rvalue是token的值
                final Claims claims = JWTUtils.parseJWT(rvalue);
                final String subject = claims.getSubject();

                // 3.1 过期，重新认证

                // 3.2 没有过期，那么直接放行
            }


        }

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (token.getPrincipal() == null) {
            return null;
        }
        final WxAccount wxAccount = wxAccountService.login(token.getUsername());
        if (wxAccount == null) {
            //这里返回后会报出对应异常
            return null;
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(token.getPrincipal(), wxAccount.getPassword(), getName());
            return simpleAuthenticationInfo;
        }
    }
}
