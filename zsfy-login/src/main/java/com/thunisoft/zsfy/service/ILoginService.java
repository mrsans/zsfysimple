package com.thunisoft.zsfy.service;

import com.alibaba.fastjson.JSONObject;
import com.thunisoft.zsfy.response.BaseResponse;

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
    BaseResponse<String> apiDoTempLogin(String loginId, String userType);

    /**
     * @deprecation: 第一次登录
     *
     * @param: username 用户名
     * @param: userType 用户类型
     * @return:
     */
    JSONObject apiLogin(String username, String password, String userType);

    /**
     * @deprecation: 验证用户名和密码
     *
     * @param:
     * @return:
     */
    boolean authPassword(String username, String password, String userType);
}
