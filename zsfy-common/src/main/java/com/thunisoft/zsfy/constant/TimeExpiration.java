package com.thunisoft.zsfy.constant;

import java.util.concurrent.TimeUnit;

/**
 * 设置生效期限
 */
public interface TimeExpiration {

    /**
     * 0秒
     */
    long ZERO_SECOND = TimeUnit.SECONDS.toSeconds(0);
    /**
     * 一天
     */
    long ONE_DAY = TimeUnit.DAYS.toSeconds(1);

    /**
     *  90天
     */
    long THREE_MONTHS = TimeUnit.DAYS.toSeconds(90);

}