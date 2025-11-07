package com.example.jwtexample.service;

import com.example.jwtexample.domain.RefreshToken;
import com.example.jwtexample.domain.UserAccount;
import com.example.jwtexample.domain.UserRole;
import com.example.jwtexample.dto.auth.LoginRequest;
import com.example.jwtexample.dto.auth.SignUpRequest;
import com.example.jwtexample.dto.auth.TokenResponse;
import com.example.jwtexample.repository.RefreshTokenRepository;
import com.example.jwtexample.repository.UserAccountRepository;
import com.example.jwtexample.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserAccountRepository userAccountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public void signUp(SignUpRequest request) {
        if (userAccountRepository.existsByUsername(request.getUsername())) {
            throw new IllegalStateException("username already exists");
        }
        UserAccount account = UserAccount.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();
        userAccountRepository.save(account);
    }

    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        return issueTokens(request.getUsername());
    }

    public TokenResponse refresh(String refreshToken) {
        RefreshToken stored = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("invalid refresh token"));
        if (stored.isExpired() || stored.isRevoked()) {
            throw new IllegalArgumentException("expired or revoked refresh token");
        }
        String username = stored.getUser().getUsername();
        refreshTokenRepository.deleteByUser(stored.getUser());
        return issueTokens(username);
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }

    private TokenResponse issueTokens(String username) {
        String access = jwtTokenProvider.generateAccessToken(username);
        String refresh = jwtTokenProvider.generateRefreshToken(username);

        UserAccount user = userAccountRepository.findByUsername(username).orElseThrow();
        RefreshToken entity = RefreshToken.builder()
                .token(refresh)
                .user(user)
                .expiresAt(Instant.now().plusSeconds(getRefreshValiditySeconds()))
                .revoked(false)
                .build();
        refreshTokenRepository.save(entity);

        return TokenResponse.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .tokenType("Bearer")
                .expiresIn(getAccessValiditySeconds())
                .build();
    }

    private long getAccessValiditySeconds() {
        return getField("access-token-validity-seconds");
    }

    private long getRefreshValiditySeconds() {
        return getField("refresh-token-validity-seconds");
    }

    private long getField(String key) {
        return switch (key) {
            case "access-token-validity-seconds" -> 900L;
            case "refresh-token-validity-seconds" -> 1209600L;
            default -> 0L;
        };
    }
}
