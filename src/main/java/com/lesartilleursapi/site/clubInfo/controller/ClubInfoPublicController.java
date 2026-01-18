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

@CrossOrigin
@RestController
@RequestMapping("/api/public/site/club-info")
public class ClubInfoPublicController {

  private final ClubInfoService clubInfoService;

  @Autowired
  public ClubInfoPublicController(ClubInfoService clubInfoService) {
    this.clubInfoService = clubInfoService;
  }

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
