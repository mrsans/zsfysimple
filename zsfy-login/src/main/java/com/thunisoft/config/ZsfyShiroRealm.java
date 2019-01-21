package com.thunisoft.config;

import com.thunisoft.bean.WxAccount;
import com.thunisoft.service.IWxAccount;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获得该用户角色
        String role = "";
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
