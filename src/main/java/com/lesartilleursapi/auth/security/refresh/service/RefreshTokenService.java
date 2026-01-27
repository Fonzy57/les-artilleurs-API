package com.lesartilleursapi.auth.security.refresh.service;

import com.lesartilleursapi.auth.security.jwt.JwtProperties;
import com.lesartilleursapi.auth.security.refresh.model.RefreshToken;
import com.lesartilleursapi.auth.security.refresh.repository.RefreshTokenRepository;
import com.lesartilleursapi.auth.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtProperties jwtProperties;
  private final SecureRandom secureRandom;

  @Autowired
  public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtProperties jwtProperties) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.jwtProperties = jwtProperties;
    this.secureRandom = new SecureRandom();
  }

  public record CreatedRefreshToken(String plainToken, RefreshToken entity) {
  }

  @Transactional
  public CreatedRefreshToken create(User user, boolean rememberMe, HttpServletRequest request) {
    String plain = generateOpaqueToken();
    String hash = sha256Hex(plain);

    Duration ttl = rememberMe ? jwtProperties.refreshTokenTtlRememberMe() : jwtProperties.refreshTokenTtl();
    Instant now = Instant.now();

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setTokenHash(hash);
    refreshToken.setUser(user);
    refreshToken.setExpiresAt(now.plus(ttl));
    refreshToken.setCreatedByIp(extractClientIp(request));
    refreshToken.setUserAgent(extractUserAgent(request));
    refreshToken.setLastUsedAt(now);

    RefreshToken saved = refreshTokenRepository.save(refreshToken);
    return new CreatedRefreshToken(plain, saved);
  }

  @Transactional
  public RefreshToken validateAndTouch(String plainToken) {
    String hash = sha256Hex(plainToken);

    Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByTokenHash(hash);

    if (refreshTokenOptional.isEmpty()) {
      throw new IllegalArgumentException("Invalid refresh token");
    }

    RefreshToken refreshToken = refreshTokenOptional.get();

    Instant now = Instant.now();
    if (refreshToken.isRevoked()) throw new IllegalArgumentException("Refresh token revoked");
    if (refreshToken.isExpired(now)) throw new IllegalArgumentException("Refresh token expired");

    refreshToken.setLastUsedAt(now);
    refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  @Transactional
  public void revoke(String plainToken) {
    String hash = sha256Hex(plainToken);

    Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByTokenHash(hash);

    if (refreshTokenOptional.isEmpty()) {
      return;
    }

    RefreshToken refreshToken = refreshTokenOptional.get();

    if (refreshToken.isRevoked()) {
      return;
    }

    refreshToken.setRevokedAt(Instant.now());
    refreshTokenRepository.save(refreshToken);
  }

  @Transactional
  public CreatedRefreshToken rotate(String plainToken, boolean rememberMe, HttpServletRequest request) {
    RefreshToken currentRefreshToken = validateAndTouch(plainToken);

    // revoke old
    currentRefreshToken.setRevokedAt(Instant.now());
    refreshTokenRepository.save(currentRefreshToken);

    User user = currentRefreshToken.getUser();
    user.getId(); // init

    // create new with same user, TTL based on remaining policy:
    return create(user, rememberMe, request);
  }


  private String generateOpaqueToken() {
    byte[] bytes = new byte[64];
    secureRandom.nextBytes(bytes);
    return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  private static String sha256Hex(String value) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] digest = md.digest(value.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(digest);
    } catch (Exception e) {
      throw new IllegalStateException("Cannot hash refresh token", e);
    }
  }

  private static String extractUserAgent(HttpServletRequest request) {
    String userAgent = request.getHeader("User-Agent");
    return userAgent != null ? userAgent : "";
  }

  private static String extractClientIp(HttpServletRequest request) {
    // simple pour commencer (faire mieux derrière proxy plus tard)
    return request.getRemoteAddr();
  }
}
