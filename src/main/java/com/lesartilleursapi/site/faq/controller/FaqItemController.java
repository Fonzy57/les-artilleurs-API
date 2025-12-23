package com.lesartilleursapi.site.faq.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/site/faq")
public class FaqItemController {

  @GetMapping
  public String getAllFaqItems() {
    return "Je retourne tous les items dans table FAQ";
  }

  @GetMapping("/{id}")
  public String getOneFaqItem(@PathVariable Long id) {
    return "Je retourne l'item du FAQ qui a l'ID : " + id;
  }

  //  TODO METTRE ANNOTATION POUR PROTEGER PAR ADMIN
  @PostMapping
  public String addOneFaqItem() {
    return "J'ajoute un item au FAQ";
  }

  //  TODO METTRE ANNOTATION POUR PROTEGER PAR ADMIN
  @PutMapping("/{id}")
  public String modifyOneFaqItem(@PathVariable Long id) {
    return "Je modifie un item du FAQ, celui qui a l'ID : " + id;
  }

  //  TODO METTRE ANNOTATION POUR PROTEGER PAR ADMIN
  @DeleteMapping("/{id}")
  public String deleteOneFaqItem(@PathVariable Long id) {
    return "Je supprime l'item du FAQ qui a l'ID : " + id;
  }
}
