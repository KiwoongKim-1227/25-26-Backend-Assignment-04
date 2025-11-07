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
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserAccountRepository userAccountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
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

    @Transactional
    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserAccount user = userAccountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("invalid username or password"));
        return issueTokens(user);
    }

    @Transactional
    public TokenResponse refresh(String refreshToken) {
        RefreshToken stored = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("invalid refresh token"));
        if (stored.isExpired() || stored.isRevoked()) {
            throw new IllegalArgumentException("expired or revoked refresh token");
        }
        UserAccount user = stored.getUser();
        refreshTokenRepository.deleteByUser(user);
        return issueTokens(user);
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken).ifPresent(RefreshToken::revoke);
    }

    private TokenResponse issueTokens(UserAccount user) {
        String access = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refresh = jwtTokenProvider.generateRefreshToken(user.getUsername());

        RefreshToken entity = RefreshToken.builder()
                .token(refresh)
                .user(user)
                .expiresAt(Instant.now().plusSeconds(jwtTokenProvider.getRefreshValiditySeconds()))
                .revoked(false)
                .build();
        refreshTokenRepository.save(entity);

        return TokenResponse.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessValiditySeconds())
                .build();
    }
}
