package com.lesartilleursapi.site.infoBlock.repository;

import com.lesartilleursapi.site.infoBlock.model.InfoBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoBlockRepository extends JpaRepository<InfoBlock, Long> {
}
