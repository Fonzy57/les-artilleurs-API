package com.lesartilleursapi.site.clubInfo.service;

import com.lesartilleursapi.site.clubInfo.dto.ClubInfoUpsertDto;
import com.lesartilleursapi.site.clubInfo.model.ClubInfo;
import com.lesartilleursapi.site.clubInfo.repository.ClubInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service layer responsible for managing club information.
 * <p>
 * This service handles the business logic related to the club's global information
 * (name, address, contact details, etc.).
 * <p>
 * The club information is treated as a singleton resource: only one
 * {@link ClubInfo} entity is expected to exist in the system at any time.
 * <p>
 * This service provides both public (read-only) and administrative
 * (create/update) operations.
 */
@Service
public class ClubInfoService {

  private final ClubInfoRepository clubInfoRepository;

  /**
   * Constructs a new {@link ClubInfoService}.
   *
   * @param clubInfoRepository repository used to access club information in the database
   */
  @Autowired
  public ClubInfoService(ClubInfoRepository clubInfoRepository) {
    this.clubInfoRepository = clubInfoRepository;
  }

  // --------------
  // --- PUBLIC ---
  // --------------

  /**
   * Retrieves the club information.
   *
   * @return an {@link Optional} containing the club information if it exists,
   * or {@link Optional#empty()} if no club information has been defined yet
   */
  public Optional<ClubInfo> getClubInfo() {
    return clubInfoRepository.findById(ClubInfo.getClubInfoId());
  }

  // -------------
  // --- ADMIN ---
  // -------------
  
  /**
   * Creates or updates the club information.
   * <p>
   * This method performs an "upsert" operation:
   * <ul>
   *   <li>If the club information does not exist, a new singleton entity is created.</li>
   *   <li>If it already exists, the existing entity is updated.</li>
   * </ul>
   * <p>
   * The persistence behavior (INSERT or UPDATE) is automatically handled
   * by JPA/Hibernate based on the entity state.
   *
   * @param dto data transfer object containing the club information
   * @return the created or updated {@link ClubInfo} entity
   */
  public ClubInfo upsertClubInfo(ClubInfoUpsertDto dto) {
    // Try to retrieve the singleton ClubInfo from the database (id = 1)
    Optional<ClubInfo> clubInfoOpt = clubInfoRepository.findById(ClubInfo.getClubInfoId());

    ClubInfo entity;

    // If no ClubInfo exists yet (empty database, first deployment, etc.)
    // we create a new singleton instance
    if (clubInfoOpt.isEmpty()) {
      entity = ClubInfo.createSingleton();
    } else {
      // Otherwise, we reuse the existing persisted entity
      entity = clubInfoOpt.get();
    }

    entity.setClubName(dto.getClubName());
    entity.setStadiumName(dto.getStadiumName());
    entity.setStreet(dto.getStreet());
    entity.setCity(dto.getCity());
    entity.setPostalCode(dto.getPostalCode());
    entity.setContactEmail(dto.getContactEmail().trim().toLowerCase());

    // Persist the entity:
    // - INSERT if the entity does not exist yet
    // - UPDATE if the entity already exists
    // JPA/Hibernate decides automatically based on the entity state
    return clubInfoRepository.save(entity);
  }
}
