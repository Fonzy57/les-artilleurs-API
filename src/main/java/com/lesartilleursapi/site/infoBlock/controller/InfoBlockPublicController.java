package com.lesartilleursapi.site.infoBlock.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.lesartilleursapi.jsonview.Views;
import com.lesartilleursapi.site.infoBlock.model.InfoBlock;
import com.lesartilleursapi.site.infoBlock.service.InfoBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/public/site/info-block")
public class InfoBlockPublicController {

  private final InfoBlockService infoBlockService;

  @Autowired
  public InfoBlockPublicController(InfoBlockService infoBlockService) {
    this.infoBlockService = infoBlockService;
  }

  @GetMapping
  @JsonView(Views.Public.class)
  public List<InfoBlock> getAllInfoBlocks() {
    return infoBlockService.getAllDisplayed();
  }
}
