package com.thunisoft.zsfy.bean;

/**
 * @Author ZhPJ
 * @Date 2019/1/22 002215:34
 * @Version 1.0
 * @Description:
 */
public enum UserType {

    COURT(2), PRO(3), UNKONW(0);

    private Integer code;

    UserType(Integer code) {
        this.code = code;
    }

    public Integer ofCode () {
        return this.code;
    }

}
