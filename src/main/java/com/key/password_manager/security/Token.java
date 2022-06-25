package com.key.password_manager.security;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class Token {

    private String username;

    private boolean isExpired;

    private Claims claims;

    public Token(String token, String secret) {
        if (Objects.nonNull(token) && !token.isEmpty()) {
            this.claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            this.isExpired = (claims.getExpiration().compareTo(Date.from(Instant.now())) >= 0);
            this.username = claims.getSubject();
        } else {
            this.claims = null;
            this.isExpired = true;
            this.username = null;
        }
    }

    public String getUsername() {
        return username;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public Claims getClaims() {
        return claims;
    }

    public Object getClaim(String key) {
        return claims.get(key);
    }

    public boolean isValid(String providedUsername) {
        return (providedUsername.equals(username));
    }
}
