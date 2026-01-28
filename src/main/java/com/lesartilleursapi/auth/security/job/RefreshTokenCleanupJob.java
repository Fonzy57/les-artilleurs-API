package com.lesartilleursapi.auth.security.job;

import com.lesartilleursapi.auth.security.refresh.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
public class RefreshTokenCleanupJob {
  private static final Duration PERIOD = Duration.ofDays(15);

  private final RefreshTokenService refreshTokenService;

  @Autowired
  public RefreshTokenCleanupJob(RefreshTokenService refreshTokenService) {
    this.refreshTokenService = refreshTokenService;
  }

  /**
   * Cleanup des refresh tokens expirés.
   * Tous les jours à 03:00 (heure serveur).
   */
  @Scheduled(cron = "0 0 3 * * *", zone = "UTC")
  public void cleanup() {
    long deletedRows = refreshTokenService.cleanupExpiredRefreshTokensOlderThan(PERIOD);

    // LOGS
    if (deletedRows > 0) {
      log.info("RefreshToken Cleanup: deleted {} tokens expired more than {} days ago.", deletedRows, PERIOD.toDays());
    } else {
      log.debug("RefreshToken cleanup : nothing to delete (period = {} days)", PERIOD.toDays());
    }
  }

}
