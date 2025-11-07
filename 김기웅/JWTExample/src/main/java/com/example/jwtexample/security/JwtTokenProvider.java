package com.example.jwtexample.security;

import com.example.jwtexample.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessValiditySeconds;
    private final long refreshValiditySeconds;
    private final CustomUserDetailsService userDetailsService;

    public JwtTokenProvider(JwtProperties props, CustomUserDetailsService userDetailsService) {
        byte[] keyBytes = decodeSecret(props.getSecret());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessValiditySeconds = props.getAccessTokenValiditySeconds();
        this.refreshValiditySeconds = props.getRefreshTokenValiditySeconds();
        this.userDetailsService = userDetailsService;
    }

    public String generateAccessToken(String username) {
        return createToken(username, accessValiditySeconds);
    }

    public String generateRefreshToken(String username) {
        return createToken(username, refreshValiditySeconds);
    }

    public Authentication getAuthentication(String token) {
        String username = parse(token).getBody().getSubject();
        UserDetails user = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    public boolean validate(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public long getAccessValiditySeconds() {
        return accessValiditySeconds;
    }

    public long getRefreshValiditySeconds() {
        return refreshValiditySeconds;
    }

    private String createToken(String username, long validitySeconds) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(validitySeconds);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
    }

    private byte[] decodeSecret(String secret) {
        try {
            return Decoders.BASE64.decode(secret);
        } catch (IllegalArgumentException ignored) {
            return secret.getBytes(StandardCharsets.UTF_8);
        }
    }
}
