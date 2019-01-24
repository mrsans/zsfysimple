package com.thunisoft.zsfy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thunisoft.dzfy.security.RSAUtil;
import com.thunisoft.dzfy.utils.MD5Util;
import com.thunisoft.zsfy.bean.RequestParams;
import com.thunisoft.zsfy.constant.APIUrls;
import com.thunisoft.zsfy.pojo.TProUser;
import com.thunisoft.zsfy.response.BaseResponse;
import com.thunisoft.zsfy.service.ILoginService;
import com.thunisoft.zsfy.utils.ZsfyRSAUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.NestedServletException;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ZhPJ
 * @Date 2019/1/22 002216:00
 * @Version 1.0
 * @Description:
 */
@Service
public class LoginServiceImpl implements ILoginService {

    private String ALGORITHMNAME = "MD5";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public BaseResponse<String> apiGetCurrectTime() {
        final ResponseEntity<String> response = restTemplate.postForEntity(APIUrls.PubAPIS.GET_CURRENTTIME, new HashMap<>(), String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return JSON.parseObject(response.getBody(), BaseResponse.class);
        }
        return null;
    }

    @Override
    public BaseResponse<String> apiDoTempLogin(String loginId, String userType) {
        final BaseResponse baseResponse = this.apiGetCurrectTime();
        final String timestamp = String.valueOf(baseResponse.getData());
        final PublicKey publicKey = RSAUtil.getPublicKey(ZsfyRSAUtil.LOGIN_WEB_PUBLIC);
        try {
            final String userTypeRSA = ZsfyRSAUtil.encrypt(publicKey, String.valueOf(userType));
            final String loginIdRSA = ZsfyRSAUtil.encrypt(publicKey, loginId);
            final String loginSign = String.format("%s%s", ZsfyRSAUtil.SGIN, timestamp);
            final String signString = MD5Util.md5Hex(loginSign);
            JSONObject keyObj = new JSONObject();
            keyObj.put("timestamp", timestamp);
            keyObj.put("sign", signString);
            final String keyRSA = ZsfyRSAUtil.encrypt(publicKey, keyObj.toString());
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("loginId", loginIdRSA);
            params.add("userType", userTypeRSA);
            params.add("key", keyRSA);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("systemid", "zsfy");
            httpHeaders.set("authcode", ZsfyRSAUtil.getAuthcode());
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, httpHeaders);
            final ResponseEntity<BaseResponse> baseResponseEntry = restTemplate.postForEntity(APIUrls.PubAPIS.AUTHENTICATION_OF_TEMPLOGIN, httpEntity, BaseResponse.class);
            if (baseResponseEntry.getStatusCode() == HttpStatus.OK) {
                return baseResponse;
            }
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public JSONObject apiLogin(String username, String password, String userType) {
        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        multiValueMap.add("userType", userType);
        multiValueMap.add("loginId", username);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(multiValueMap, null);
        final String result = restTemplate.postForObject(APIUrls.PubAPIS.PROUSERBYLOGINID, httpEntity, String.class);
        final JSONObject jsonObject = JSON.parseObject(result);
        if (jsonObject.getBoolean("success")) {
            return jsonObject;
        }
        return null;
    }

    /**
     * @deprecation: 验证用户名和密码
     *
     * @param:
     * @return:
     */
    @Override
    public boolean authPassword(RequestParams params) {
        boolean isEqPwd = false;
        String aim = params.getAim();
        switch (aim) {
            case "dsrZjhmLogin"://当事人证件号码登录
                isEqPwd = this.xcxLoginByZjhm(params.getUserType(),params.getLoginType(),params.getName(),params.getZjhm(),params.getXcxOpenId());
                break;
            case "dsrCommonLogin"://当事人账号密码登录
                isEqPwd = this.dsrLogin(params.getUsername(),params.getPassword(),params.getUserType());
                break;
            case "lsLogin"://律师登录
                lsLogin();
                break;
            case "fgLogin"://法官登录
                fgLogin();
                break;
            case "dsrAutoLogin"://当事人自动登录
                break;
            case "fgAutoLogin" ://法官自动登录
                break;
            case "lsAutoLogin"://律师自动登录
                break;
            default:
                break;
        }
        return isEqPwd;
    }

    private boolean xcxLoginByZjhm(String userType, String loginType, String name, String zjhm, String xcxOpenId) {

        return false;
    }

    /**
     * @deprecation: 当事人登录
     *
     * @param:  username ： 用户名
     * @param:  passowrd ： 密码
     * @param:  userType ： 用户类型
     * @return:
     */
    private boolean dsrLogin(String username, String password, String userType) {
        boolean isEqPwd = false;
        SimpleHash simpleHash = new SimpleHash(this.ALGORITHMNAME, password, null, 0);
        final JSONObject jsonObject = this.apiLogin(username, password, userType);
        if (jsonObject != null) {
            final String data = jsonObject.getString("data");
            if (StringUtils.isNotBlank(data)) {
                List<TProUser> userList = JSON.parseArray(data, TProUser.class);
                final String cPassword = userList.get(0).getCPassword();
                isEqPwd = StringUtils.equalsIgnoreCase(simpleHash.toString(), cPassword);
            }
        }
        return isEqPwd;
    }

}
