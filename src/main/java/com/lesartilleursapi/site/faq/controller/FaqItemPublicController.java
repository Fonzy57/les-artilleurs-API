package com.lesartilleursapi.site.faq.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.lesartilleursapi.jsonview.Views;
import com.lesartilleursapi.site.faq.model.FaqItem;
import com.lesartilleursapi.site.faq.service.FaqItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Public REST controller for FAQ items.
 * <p>
 * This controller exposes read-only endpoints intended for public access.
 * It allows clients to retrieve the list of FAQ items or a single FAQ item by its identifier.
 * <p>
 * All endpoints under this controller are publicly accessible and do not require authentication.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/public/site/faq")
public class FaqItemPublicController {

  private final FaqItemService faqItemService;

  /**
   * Constructs a new {@link FaqItemPublicController}.
   *
   * @param faqItemService service responsible for FAQ business logic
   */
  public FaqItemPublicController(FaqItemService faqItemService) {
    this.faqItemService = faqItemService;
  }

  /**
   * Retrieves all FAQ items.
   *
   * @return a {@link ResponseEntity} containing the list of FAQ items.
   * Returns an empty list if no FAQ items are found.
   */
  @GetMapping
  @JsonView(Views.Public.class)
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
  @JsonView(Views.Public.class)
  public ResponseEntity<FaqItem> getOneFaqItem(@PathVariable Long id) {
    Optional<FaqItem> faqItem = faqItemService.getOne(id);
    if (faqItem.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(faqItem.get());
  }
}
