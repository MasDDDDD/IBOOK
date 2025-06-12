package com.huawei.ibookstudy.util;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.huawei.ibookstudy.model.StudentDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;

public class JWTUtils {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);
    private static final String SECRET = "software_management_913";
    private static final long EXPIRATION = 1000L;
    public static String createToken(StudentDo student, int roleId) {
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000 * 60);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        return JWT.create()
                .withHeader(map)
                .withClaim("stuNum", student.getStuNum())
                .withClaim("name", student.getName())
                .withClaim("roleId", roleId)
                .withExpiresAt(expireDate)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC512(SECRET));
    }
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error("Token decoding error");
            return null;
        }
        return jwt.getClaims();
    }
}
