package com.lesartilleursapi.site.infoBlock.controller;

import com.lesartilleursapi.site.infoBlock.service.InfoBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/public/site/info-block")
public class InfoBlockPublicController {

  private final InfoBlockService infoBlockService;

  @Autowired
  public InfoBlockPublicController(InfoBlockService infoBlockService) {
    this.infoBlockService = infoBlockService;
  }
}
