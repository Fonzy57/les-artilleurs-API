package com.lesartilleursapi.site.faq.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.lesartilleursapi.jsonview.Views;
import com.lesartilleursapi.site.faq.dto.FaqItemCreateDto;
import com.lesartilleursapi.site.faq.dto.FaqItemUpdateDto;
import com.lesartilleursapi.site.faq.model.FaqItem;
import com.lesartilleursapi.site.faq.service.FaqItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//  TODO METTRE ANNOTATION POUR PROTÉGER PAR ADMIN

/**
 * Administrative REST controller for FAQ items.
 * <p>
 * This controller exposes write operations (create, update, delete) intended
 * for administrative usage only.
 * <p>
 * Endpoints under this controller should be protected by authentication and
 * authorization mechanisms (e.g. ADMIN role).
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/faq")
public class FaqItemAdminController {

  private final FaqItemService faqItemService;

  /**
   * Constructs a new {@link FaqItemAdminController}.
   *
   * @param faqItemService service responsible for FAQ business logic
   */
  @Autowired
  public FaqItemAdminController(FaqItemService faqItemService) {
    this.faqItemService = faqItemService;
  }


  /**
   * Retrieves all FAQ items.
   *
   * @return a {@link ResponseEntity} containing the list of FAQ items.
   * Returns an empty list if no FAQ items are found.
   */
  @GetMapping
  @JsonView(Views.Admin.class)
  public ResponseEntity<List<FaqItem>> getAllFaqItems() {
    List<FaqItem> faqItems = faqItemService.getAll();
    return ResponseEntity.ok(faqItems);
  }

  /**
   * Retrieves a single FAQ item by its identifier.
   *
   * @param id the identifier of the FAQ item
   * @return a {@link ResponseEntity} containing the FAQ item if found,
   * or {@code 404 Not Found} if the item does not exist
   */
  @GetMapping("/{id}")
  @JsonView(Views.Admin.class)
  public ResponseEntity<FaqItem> getOneFaqItem(@PathVariable Long id) {
    Optional<FaqItem> faqItem = faqItemService.getOne(id);
    if (faqItem.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(faqItem.get());
  }

  /**
   * Creates a new FAQ item.
   *
   * @param faqItemAddDto data transfer object containing the FAQ item information
   * @return a {@link ResponseEntity} containing the created FAQ item
   * with HTTP status {@code 201 Created}
   */
  @PostMapping
  @JsonView(Views.Admin.class)
  public ResponseEntity<FaqItem> addOneFaqItem(@RequestBody @Valid FaqItemCreateDto faqItemAddDto) {
    FaqItem created = faqItemService.addOne(faqItemAddDto);
    return ResponseEntity.status(201).body(created);
  }

  /**
   * Updates an existing FAQ item.
   *
   * @param id               the identifier of the FAQ item to update
   * @param faqItemUpdateDto data transfer object containing the updated FAQ item information
   * @return a {@link ResponseEntity} containing the updated FAQ item if found,
   * or {@code 404 Not Found} if the item does not exist
   */
  @PutMapping("/{id}")
  @JsonView(Views.Admin.class)
  public ResponseEntity<FaqItem> updateOneFaqItem(@PathVariable Long id,
      @RequestBody @Valid FaqItemUpdateDto faqItemUpdateDto) {
    Optional<FaqItem> updatedItem = faqItemService.updateOne(id, faqItemUpdateDto);

    if (updatedItem.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(updatedItem.get());
  }

  /**
   * Deletes a FAQ item by its identifier.
   *
   * @param id the identifier of the FAQ item to delete
   * @return {@code 204 No Content} if the deletion was successful,
   * or {@code 404 Not Found} if the item does not exist
   */
  @DeleteMapping("/{id}")
  @JsonView(Views.Admin.class)
  public ResponseEntity<Void> deleteOneFaqItem(@PathVariable Long id) {
    Optional<FaqItem> deletedItem = faqItemService.deleteOne(id);

    if (deletedItem.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.noContent().build();
  }
}
