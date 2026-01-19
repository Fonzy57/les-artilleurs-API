package com.lesartilleursapi.site.clubInfo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.lesartilleursapi.jsonview.Views;
import com.lesartilleursapi.site.clubInfo.model.ClubInfo;
import com.lesartilleursapi.site.clubInfo.service.ClubInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Public REST controller for club information.
 * <p>
 * This controller exposes a read-only endpoint intended for public access,
 * allowing clients to retrieve the club's global information.
 * <p>
 * The data returned by this controller is restricted to public fields only,
 * using {@link JsonView} to prevent exposure of sensitive information.
 * <p>
 * All endpoints under this controller are publicly accessible and do not
 * require authentication.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/public/site/club-info")
public class ClubInfoPublicController {

  private final ClubInfoService clubInfoService;

  /**
   * Constructs a new {@link ClubInfoPublicController}.
   *
   * @param clubInfoService service responsible for club information business logic
   */
  @Autowired
  public ClubInfoPublicController(ClubInfoService clubInfoService) {
    this.clubInfoService = clubInfoService;
  }

  /**
   * Retrieves the club information.
   *
   * @return a {@link ResponseEntity} containing the club information if it exists,
   * or {@code 404 Not Found} if no club information has been defined yet
   */
  @GetMapping
  @JsonView(Views.Public.class)
  public ResponseEntity<ClubInfo> getClubInfo() {
    Optional<ClubInfo> clubInfo = clubInfoService.getClubInfo();

    if (clubInfo.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(clubInfo.get());
  }

}
