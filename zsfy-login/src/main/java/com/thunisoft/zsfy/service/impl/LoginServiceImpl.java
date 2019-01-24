package com.thunisoft.zsfy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thunisoft.dzfy.security.RSAUtil;
import com.thunisoft.dzfy.utils.MD5Util;
import com.thunisoft.zsfy.bean.LoginBean;
import com.thunisoft.zsfy.constant.APIUrls;
import com.thunisoft.zsfy.response.BaseResponse;
import com.thunisoft.zsfy.service.ILoginService;
import com.thunisoft.zsfy.utils.ZsfyRSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author ZhPJ
 * @Date 2019/1/22 002216:00
 * @Version 1.0
 * @Description:
 */
@Service
public class LoginServiceImpl implements ILoginService {

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
    public String apiDoTempLogin(String loginId, String userType) {
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
            Map<String, String> params = new HashMap<>();
            params.put("loginId", loginIdRSA);
            params.put("userType", userTypeRSA);
            params.put("key", keyRSA);
            HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(params, null);
            final ResponseEntity<String> responseEntity1 = restTemplate.postForEntity(APIUrls.PubAPIS.AUTHENTICATION_OF_TEMPLOGIN, httpEntity, String.class);
            final String s = restTemplate.postForObject(APIUrls.PubAPIS.AUTHENTICATION_OF_TEMPLOGIN, params, String.class);
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}
