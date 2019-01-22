package com.thunisoft.zsfy.config;

import com.thunisoft.zsfy.bean.WxAccount;
import com.thunisoft.zsfy.service.IWxAccount;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author ZhPJ
 * @Date 2019/1/15 001512:04
 * @Version 1.0
 * @Description:
 */
public class ZsfyShiroRealm extends AuthorizingRealm {

    @Autowired
    private IWxAccount wxAccountService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        /*String username = (String) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获得该用户角色
        String role = "";
        Set<String> set = new HashSet<>();
        //需要将 role 封装到 Set 作为 info.setRoles() 的参数
        set.add(role);
        //设置该用户拥有的角色
        info.setRoles(set);
        return info;*/
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (token.getPrincipal() == null) {
            throw new UnknownAccountException();
        }
        final WxAccount wxAccount = wxAccountService.login(token.getUsername());
        if (wxAccount == null) {
            //这里返回后会报出对应异常
            throw new UnknownAccountException();
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(token.getPrincipal(), wxAccount.getPassword(), getName());
            return simpleAuthenticationInfo;
        }
    }
}
