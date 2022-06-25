package com.key.password_manager.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTGenerator {

    @Value("${com.keys.jwtprop.secret_key}")
    private String SECRET_KEY;

    @Value("${com.keys.jwtprop.expiration}")
    private Long DEFAULT_EXPIRATION_TIME_LIMIT;

    public String generate(String email) throws Exception {
        if (Objects.isNull(email) || email.isEmpty()) {
            throw new Exception("Email cannot be empty or null.");
        }
        return generate(new HashMap<String, Object>(), email, DEFAULT_EXPIRATION_TIME_LIMIT);
    }

    public String generate(String email, long expiration) throws Exception {
        if (expiration < 0) {
            throw new Exception("Expiration cannot be before the current time.");
        }
        return generate(new HashMap<String, Object>(), email, expiration);
    }

    private String generate(Map<String, Object> claims, String email, long expiration) {
        return Jwts.builder().setClaims(claims).setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + expiration)).compact();
    }
}
