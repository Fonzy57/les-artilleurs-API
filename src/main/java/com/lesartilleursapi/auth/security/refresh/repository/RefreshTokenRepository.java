package com.lesartilleursapi.auth.security.refresh.repository;

import com.lesartilleursapi.auth.security.refresh.model.RefreshToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  @EntityGraph(attributePaths = {"user", "user.role"})
  Optional<RefreshToken> findByTokenHash(String tokenHash);

  long deleteByExpiresAtBefore(Instant cutoff);
}
