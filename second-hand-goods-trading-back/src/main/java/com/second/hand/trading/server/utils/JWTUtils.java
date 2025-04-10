package com.second.hand.trading.server.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JWTUtils {
    private static final String SECRET_KEY = "your-jwt-secret"; // 自定义密钥
    private static final long EXPIRATION_TIME = 86400000; // 24小时

    public static String generateToken(String openid) {
        return Jwts.builder()
                .setSubject(openid)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static String validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // 返回openid
    }
}
