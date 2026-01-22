package com.lesartilleursapi.auth.security.jwt;

import com.lesartilleursapi.auth.security.userdetails.UserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtService {

  private final JwtProperties props;
  private final SecretKey key;

  public JwtService(JwtProperties props) {
    this.props = props;
    byte[] secret = Base64.getDecoder().decode(props.secretBase64());
    this.key = Keys.hmacShaKeyFor(secret);
  }

  /**
   * Version "bas niveau" si tu veux signer n'importe quel sujet + claims
   */
  public String generateAccessToken(String subject, Map<String, Object> claims) {
    Instant now = Instant.now();
    Instant exp = now.plus(props.accessTokenTtl());

    return Jwts.builder()
        .subject(subject)
        .issuer(props.issuer())
        .issuedAt(Date.from(now))
        .expiration(Date.from(exp))
        .claims(new LinkedHashMap<>(claims))
        .signWith(key)
        .compact();
  }

  /**
   * Version recommandée : token à partir du principal
   */
  public String generateAccessToken(UserPrincipal principal) {
    Map<String, Object> claims = new LinkedHashMap<>();
    claims.put("id", principal.getId());
    claims.put("firstname", principal.getFirstname());
    claims.put("lastname", principal.getLastname());
    claims.put("role", principal.getRoleCode());

    // subject = email pour l’instant (simple)
    return generateAccessToken(principal.getEmail(), claims);
  }

  public String getSubject(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public void validate(String token) {
    Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
  }
}
