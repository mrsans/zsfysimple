package com.thunisoft.zsfy.service;

import com.thunisoft.zsfy.bean.WxAccount;

/**
 * @Author ZhPJ
 * @Date 2019/1/17 001717:42
 * @Version 1.0
 * @Description:
 */
public interface IWxAccount {

    WxAccount login(String username);

}