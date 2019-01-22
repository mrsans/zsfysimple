package com.thunisoft.zsfy.utils;

import java.util.UUID;

/**
 * @Author ZhPJ
 * @Date 2019/1/16 001614:22
 * @Version 1.0
 * @Description:
 */
public abstract class UUIDHelper {

    /**
     * @deprecation: 带有分隔线的uuid：如：7dc4183e-37be-455b-bda7-05d5e64e0b62
     *
     * @param: num 是需要的个数，一般java帮助类的uuid个数为 36位，当输入0或者大于36位时，直接返回36位uuid
     * @return: 返回对应的uuid
     */
    public static final String getUUIDHasDivider(int num) {
        final String uuid = UUID.randomUUID().toString();
        if (num < 1 || num > uuid.length()) {
            return uuid;
        }
        return uuid.substring(0, num);
    }

    /**
     * @deprecation: 32位uuid生成，没有分隔线，如：9fc2adbb63374c439e52b24e36361ea0
     *
     * @param: num 是需要的个数，一般java帮助类的uuid个数为 32位，当输入0或者大于32位时，直接返回32位uuid
     * @return: 返回对应的uuid
     */
    public static final  String getUUIDNoDivider(int num) {
        final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        if (num < 1 || num > uuid.length()) {
            return uuid;
        }
        return uuid.substring(0, num);
    }

    public static final  String getUUIDNoDivider() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
