package com.thunisoft.zsfy.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author ZhPJ
 * @Date 2019/1/17 001714:03
 * @Version 1.0
 * @Description:
 */
@Getter
@Setter
public class WxAccount implements Serializable {
    // id
    private String id;
    // 用户名
    private String username;
    // 密码
    private transient String password;
    // 签发者
    private String iss;
    // 过期时间
    private String exp;
    // 主题
    private String sub;

}
