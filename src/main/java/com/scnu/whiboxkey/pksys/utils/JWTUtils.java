package com.scnu.whiboxkey.pksys.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JwtToken生成的工具类
 * JWT token的格式：header.payload.signature
 * header的格式（算法、token的类型）,默认：{"alg": "HS512","typ": "JWT"}
 * payload的格式 设置：（用户信息、创建时间、生成时间）
 * signature的生成算法： HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTUtils {

    //定义token返回头部
    public static String header;

    //token前缀
    public static String tokenPrefix;

    //签名密钥
    public static String secret;

    //有效期
    public static long expireTime;

    //存进header的token的key名
    public static final String AUTH_TOKEN = "AUTH_TOKEN";

    public void setHeader(String header) {
        JWTUtils.header = header;
    }

    public void setTokenPrefix(String tokenPrefix) {
        JWTUtils.tokenPrefix = tokenPrefix;
    }

    public void setSecret(String secret) {
        JWTUtils.secret = secret;
    }

    public void setExpireTime(int expireTimeInt) {
        JWTUtils.expireTime = expireTimeInt*1000L*60;
    }

    /**
     * 创建TOKEN
     */
    public static String createToken(String serial,String ip){
        return tokenPrefix + JWT.create()
                //项目名
                .withSubject("whiboxkey-sys")
                //设置内容信息
                .withClaim("serial",serial)
                .withClaim("ip",ip)
                //设置过期时间
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
                //认证信息
                .sign(Algorithm.HMAC512(secret));
    }


    /**
     * 验证token
     * 验证token中携带的serial和ip和传入的参数相同
     * 验证通过为true，验证失败为false
     */
    public static Boolean validateToken(String token, String Serial, String IP){
        try {
            Map<String, Claim> claims = JWT.require(Algorithm.HMAC512(secret))
                                .build()
                                .verify(token.replace(tokenPrefix, ""))
                                .getClaims();
            return claims.get("serial").asString().equals(Serial)
                    && claims.get("ip").asString().equals(IP);
        } catch (Exception e){
            return false;
        }
    }

    /**
     * 检查token是否需要更新
     */
    public static boolean isNeedUpdate(String token){
        //获取token过期时间
        Date expiresAt = null;
        try {
            expiresAt = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getExpiresAt();
        } catch (TokenExpiredException e){
            return true;
        }
        //如果剩余过期时间少于过期时常的一般时 需要更新
        return (expiresAt.getTime()-System.currentTimeMillis()) < (expireTime>>1);
    }
}