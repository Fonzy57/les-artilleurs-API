package com.lesartilleursapi.site.clubInfo.service;

import com.lesartilleursapi.site.clubInfo.model.ClubInfo;
import com.lesartilleursapi.site.clubInfo.repository.ClubInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClubInfoService {

  private final ClubInfoRepository clubInfoRepository;

  @Autowired
  public ClubInfoService(ClubInfoRepository clubInfoRepository) {
    this.clubInfoRepository = clubInfoRepository;
  }

  // --------------
  // --- PUBLIC ---
  // --------------

  public Optional<ClubInfo> getClubInfo() {
    return clubInfoRepository.findById(ClubInfo.getClubInfoId());
  }

  // -------------
  // --- ADMIN ---
  // -------------
}
