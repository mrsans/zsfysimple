package com.thunisoft.zsfy.utils;

import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @Author ZhPJ
 * @Date 2019/1/18 001815:13
 * @Version 1.0
 * @Description:
 */
public class JWTUtilsTest {

    private final static String TEST_SUB = "test1";

    @Test
    public void createOrParseJWT() {
        final String jwt = JWTUtils.createJWT(TEST_SUB);
        final Claims claims = JWTUtils.parseJWT(jwt);
        System.out.println(claims.getSubject());
        assertTrue("jwt生成或解析失败", StringUtils.equals(claims.getSubject(), TEST_SUB));
    }

    @Test
    public void test1 () {
        final String format = String.format("%s_%s", "222", null);
        System.out.println(format);
    }

}