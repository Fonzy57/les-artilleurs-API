package com.lesartilleursapi.site.clubInfo.service;

import com.lesartilleursapi.site.clubInfo.dto.ClubInfoUpsertDto;
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
  public ClubInfo updateClubInfo(ClubInfoUpsertDto dto) {
    Optional<ClubInfo> clubInfoOpt = clubInfoRepository.findById(ClubInfo.getClubInfoId());

    ClubInfo entity;

    if (clubInfoOpt.isEmpty()) {
      entity = ClubInfo.createSingleton();
    } else {
      entity = clubInfoOpt.get();
    }

    entity.setClubName(dto.getClubName());
    entity.setStadiumName(dto.getStadiumName());
    entity.setStreet(dto.getStreet());
    entity.setCity(dto.getCity());
    entity.setPostalCode(dto.getPostalCode());
    entity.setContactEmail(dto.getContactEmail().trim().toLowerCase());

    return clubInfoRepository.save(entity);
  }
}
