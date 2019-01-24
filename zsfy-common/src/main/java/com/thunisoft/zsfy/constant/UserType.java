package com.thunisoft.zsfy.constant;

/**
 * @Author ZhPJ
 * @Date 2019/1/22 002213:14
 * @Version 1.0
 * @Description:
 */
public enum UserType {

    FG(1, "法官"), DSR(2, "当事人"), LS(3, "律师"), UNKONW(0, "未知");

    private Integer code;
    private String value;

    UserType(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer ofCode() {
        return code;
    }

    public String ofValue() {
        return value;
    }

}
