package com.lesartilleursapi.site.faq.controller;

import com.lesartilleursapi.site.faq.model.FaqItem;
import com.lesartilleursapi.site.faq.service.FaqItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/faq")
public class FaqItemAdminController {

  private final FaqItemService faqItemService;

  public FaqItemAdminController(FaqItemService faqItemService) {
    this.faqItemService = faqItemService;
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
  public ResponseEntity<Void> deleteOneFaqItem(@PathVariable Long id) {
    Optional<FaqItem> deletedItem = faqItemService.deleteOne(id);

    if (deletedItem.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.noContent().build();
  }
}
