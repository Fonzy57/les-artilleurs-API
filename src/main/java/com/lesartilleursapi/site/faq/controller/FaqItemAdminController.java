package com.lesartilleursapi.site.faq.controller;

import com.lesartilleursapi.site.faq.dto.FaqItemAddDto;
import com.lesartilleursapi.site.faq.dto.FaqItemUpdateDto;
import com.lesartilleursapi.site.faq.model.FaqItem;
import com.lesartilleursapi.site.faq.service.FaqItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//  TODO METTRE ANNOTATION POUR PROTEGER PAR ADMIN
@CrossOrigin
@RestController
@RequestMapping("/api/admin/faq")
public class FaqItemAdminController {

  private final FaqItemService faqItemService;

  public FaqItemAdminController(FaqItemService faqItemService) {
    this.faqItemService = faqItemService;
  }

  @PostMapping
  public ResponseEntity<FaqItem> addOneFaqItem(@RequestBody @Valid FaqItemAddDto faqItemAddDto) {
    FaqItem created = faqItemService.addOne(faqItemAddDto);
    return ResponseEntity.status(201).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<FaqItem> modifyOneFaqItem(@PathVariable Long id,
      @RequestBody @Valid FaqItemUpdateDto faqItemUpdateDto) {
    Optional<FaqItem> updatedItem = faqItemService.modifyOne(id, faqItemUpdateDto);

    if (updatedItem.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(updatedItem.get());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOneFaqItem(@PathVariable Long id) {
    Optional<FaqItem> deletedItem = faqItemService.deleteOne(id);

    if (deletedItem.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.noContent().build();
  }
}
