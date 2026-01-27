package com.lesartilleursapi.auth.security.refresh.repository;

import com.lesartilleursapi.auth.security.refresh.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByTokenHash(String tokenHash);

  // Supprime tous les RefreshToken dont expiresAt est antérieur à now, et retourne combien ont été supprimés
  long deleteByExpiresAtBefore(Instant now);
}
