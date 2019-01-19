package com.thunisoft.config;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ZhPJ
 * @Date 2019/1/15 001510:10
 * @Version 1.0
 * @Description:
 */
@Configuration
public class ShiroConfiguration extends DefaultWebSessionManager {

    //将自己的验证方式加入容器
    @Bean
    public ZsfyShiroRealm myShiroRealm() {
        ZsfyShiroRealm zsfyShiroRealm = new ZsfyShiroRealm();
        return zsfyShiroRealm;
    }



    @Bean(value = "securityManger")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        // 不存储会话
//        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
//        sessionStorageEvaluator.setSessionStorageEnabled(false);

//        DefaultSessionManager sessionManager = new DefaultSessionManager();
//        sessionManager.setSessionValidationSchedulerEnabled(false);
//        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }
    /**
     * 禁止使用session
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator(){
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String,String> chainsMap = new HashMap<String, String>();
        //登出
//        chainsMap.put("/logout","logout");
        //登录
        shiroFilterFactoryBean.setLoginUrl("/login/**");
        //首页
//        shiroFilterFactoryBean.setSuccessUrl("/index");
        //错误页面，认证不通过跳转
//        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        //对所有用户认证
        chainsMap.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(chainsMap);
        return shiroFilterFactoryBean;
    }

    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
