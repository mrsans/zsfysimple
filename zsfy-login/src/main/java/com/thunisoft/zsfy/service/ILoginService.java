package com.thunisoft.zsfy.service;

import com.thunisoft.zsfy.bean.LoginBean;
import com.thunisoft.zsfy.response.BaseResponse;

import java.io.IOException;

/**
 * @Author ZhPJ
 * @Date 2019/1/22 002215:58
 * @Version 1.0
 * @Description:
 */
public interface ILoginService {

    BaseResponse apiGetCurrectTime();

    /**
     * @deprecation: 获取登录信息
     *
     * @param: loginBean 登录实体
     * @return: success: 是否成功  true || false"
     *     "message": "提示信息"
     *     "data": "当前用户的sessionId"
     */
    String apiDoTempLogin(String  loginId, String userType);


}
