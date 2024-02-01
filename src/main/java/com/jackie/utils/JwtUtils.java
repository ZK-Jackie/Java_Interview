package com.jackie.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/*生成/解析JWT令牌，会话依赖项*/
public class JwtUtils {

    private static String signKey = "JsonTokenJsonToken1JsonToken2JsonTokenJsonToken1JsonToken2";
    private static Long expire = 43200000L;

    public static String generateJwt(Map<String, Object> claims){
        String jwt = Jwts.builder() //jwt构造器，其实应该就是new了一个jwt
                .setClaims(claims)  //数据（为什么要叫claims？）
                .signWith(SignatureAlgorithm.HS256, signKey)    //加密算法，签名（密钥）
                .setExpiration(new Date(System.currentTimeMillis() + expire))                //有效日期
                .compact();    //变成字符串
        return jwt;
    }

    public static Claims parseJwt(String jwt) throws JwtException {
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
}
