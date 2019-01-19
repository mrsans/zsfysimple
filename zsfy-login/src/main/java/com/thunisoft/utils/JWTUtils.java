package com.thunisoft.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import sun.swing.plaf.synth.DefaultSynthStyle;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author ZhPJ
 * @Date 2019/1/18 001811:10
 * @Version 1.0
 * @Description:
 */
public abstract class JWTUtils {

    private final static Key SIGN_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final static long EX_TIME = TimeUnit.DAYS.toSeconds(1);

    private final static String ISSUER = "zsfy";

    /**
     * @describtion: 创建一个jwt
     * @param: subject 用户信息
     * @return: 得到jwt
     */
    public static String createJWT(String subject) {
        final long extime = System.currentTimeMillis() + EX_TIME;
        String jws = Jwts.builder()
                .setSubject(subject)
                .setIssuer(ISSUER)
                .setExpiration(new Date(extime))
                .signWith(SIGN_KEY)
                .compact();
        return jws;
    }

    /**
     * @describtion: 解析jwt
     * @param: jwt
     * @return: 获取jwt解析后的结构体
     */
    public static Claims parseJWT(String jwt) {
        final Claims claims = Jwts.parser().setSigningKey(SIGN_KEY).parseClaimsJws(jwt).getBody();
        return claims;
    }

}
