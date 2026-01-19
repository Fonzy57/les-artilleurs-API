package com.lesartilleursapi.site.clubInfo.repository;

import com.lesartilleursapi.site.clubInfo.model.ClubInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubInfoRepository extends JpaRepository<ClubInfo, Long> {
}
