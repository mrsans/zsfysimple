package com.thunisoft.zsfy.constant;

import java.util.concurrent.TimeUnit;

/**
 * @Author ZhPJ
 * @Date 2019/1/21 002116:42
 * @Version 1.0
 * @Description:
 */
public interface Constants {

    /**
     * 关于设置客户端的Cookie键值
     */
    interface CookiesKey {

        /**
         * 给前台设置cookie的值
         */
        String TOKEN_AUTH = "token_auth";

    }

    /**
     * redis的过期时间
     */
    interface TimeExpiration {

        /**
         * 设置redis的过期时间
         */
        long ONE_DAY = TimeUnit.DAYS.toSeconds(1);

    }




}
