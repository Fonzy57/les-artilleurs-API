package com.lesartilleursapi.site.infoBlock.service;

import com.lesartilleursapi.site.infoBlock.repository.InfoBlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoBlockService {

  private final InfoBlockRepository infoBlockRepository;

  @Autowired
  public InfoBlockService(InfoBlockRepository infoBlockRepository) {
    this.infoBlockRepository = infoBlockRepository;
  }
}
