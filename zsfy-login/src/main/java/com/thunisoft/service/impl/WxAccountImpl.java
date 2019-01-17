package com.thunisoft.service.impl;

import com.thunisoft.bean.WxAccount;
import com.thunisoft.dao.WxAccountDao;
import com.thunisoft.service.IWxAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author ZhPJ
 * @Date 2019/1/17 001717:42
 * @Version 1.0
 * @Description:
 */
@Service
public class WxAccountImpl implements IWxAccount {

    @Autowired
    private WxAccountDao wxAccountDao;

    @Override
    public WxAccount login(String username) {

        return wxAccountDao.login(username);
    }
}
