package com.lesartilleursapi.site.clubInfo.controller;

//  TODO METTRE ANNOTATION POUR PROTÉGER PAR ADMIN

import com.fasterxml.jackson.annotation.JsonView;
import com.lesartilleursapi.jsonview.Views;
import com.lesartilleursapi.site.clubInfo.dto.ClubInfoUpsertDto;
import com.lesartilleursapi.site.clubInfo.model.ClubInfo;
import com.lesartilleursapi.site.clubInfo.service.ClubInfoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Administrative REST controller for club information management.
 * <p>
 * This controller exposes endpoints used to retrieve and update the club's
 * global information (such as name, address, contact details, etc.).
 * <p>
 * The club information is handled as a singleton resource, meaning that
 * only one instance of {@link ClubInfo} is expected to exist in the system.
 * <p>
 * All endpoints under this controller are intended for administrative use
 * and should be protected by appropriate authentication and authorization
 * mechanisms (e.g. ADMIN role).
 */
@CrossOrigin
@RestController
@RequestMapping("/api/admin/club-info")
public class ClubInfoAdminController {

  private final ClubInfoService clubInfoService;

  /**
   * Constructs a new {@link ClubInfoAdminController}.
   *
   * @param clubInfoService service responsible for club information business logic
   */
  @Autowired
  public ClubInfoAdminController(ClubInfoService clubInfoService) {
    this.clubInfoService = clubInfoService;
  }

  /**
   * Retrieves the club information.
   *
   * @return a {@link ResponseEntity} containing the club information if it exists,
   * or {@code 404 Not Found} if no club information has been defined yet
   */
  @GetMapping
  @JsonView(Views.Admin.class)
  public ResponseEntity<ClubInfo> getClubInfo() {
    Optional<ClubInfo> clubInfo = clubInfoService.getClubInfo();

    if (clubInfo.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(clubInfo.get());
  }

  /**
   * Creates or updates the club information.
   * <p>
   * This operation performs an "upsert": if the club information does not exist,
   * it will be created; otherwise, the existing record will be updated.
   *
   * @param clubInfoUpsertDto data transfer object containing the club information
   * @return a {@link ResponseEntity} containing the created or updated club information
   */
  @PutMapping
  @JsonView(Views.Admin.class)
  public ResponseEntity<ClubInfo> upsertClubInfo(@RequestBody @Valid ClubInfoUpsertDto clubInfoUpsertDto) {
    ClubInfo upsertedClubInfo = clubInfoService.upsertClubInfo(clubInfoUpsertDto);
    return ResponseEntity.ok(upsertedClubInfo);
  }
}
