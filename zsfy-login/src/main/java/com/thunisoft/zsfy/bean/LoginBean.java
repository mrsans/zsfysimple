package com.thunisoft.zsfy.bean;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Author ZhPJ
 * @Date 2019/1/22 002215:32
 * @Version 1.0
 * @Description: 登录实体
 */
@Data
public class LoginBean {

    private String loginId;

    private String userType;

    private Key key;



}
