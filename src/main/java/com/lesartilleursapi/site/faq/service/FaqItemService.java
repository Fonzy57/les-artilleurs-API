package com.lesartilleursapi.site.faq.service;

import com.lesartilleursapi.site.faq.dto.FaqItemAddDto;
import com.lesartilleursapi.site.faq.dto.FaqItemUpdateDto;
import com.lesartilleursapi.site.faq.model.FaqItem;
import com.lesartilleursapi.site.faq.repository.FaqItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer responsible for FAQ item business logic.
 * <p>
 * This service provides operations for both public (read-only) and administrative
 * (create, update, delete) use cases.
 * <p>
 * It acts as an intermediary between controllers and the persistence layer,
 * encapsulating data access and business rules.
 */
@Service
public class FaqItemService {

  private final FaqItemRepository faqItemRepository;

  /**
   * Constructs a new {@link FaqItemService}.
   *
   * @param faqItemRepository repository used to access FAQ items in the database
   */
  @Autowired
  public FaqItemService(FaqItemRepository faqItemRepository) {
    this.faqItemRepository = faqItemRepository;
  }

  // --------------
  // --- PUBLIC ---
  // --------------

  /**
   * Retrieves all FAQ items.
   *
   * @return a list of all FAQ items.
   * Returns an empty list if no FAQ items are found.
   */
  public List<FaqItem> getAll() {
    return faqItemRepository.findAll();
  }

  /**
   * Retrieves a single FAQ item by its identifier.
   *
   * @param id the identifier of the FAQ item
   * @return an {@link Optional} containing the FAQ item if found,
   * or {@link Optional#empty()} if no item exists with the given identifier
   */
  public Optional<FaqItem> getOne(Long id) {
    return faqItemRepository.findById(id);
  }

  // -------------
  // --- ADMIN ---
  // -------------

  /**
   * Creates a new FAQ item.
   *
   * @param dto data transfer object containing the FAQ item information
   * @return the newly created {@link FaqItem}
   */
  public FaqItem addOne(FaqItemAddDto dto) {
    FaqItem faqItem = new FaqItem();
    faqItem.setId(null);
    faqItem.setQuestion(dto.getQuestion());
    faqItem.setAnswer(dto.getAnswer());

    return faqItemRepository.save(faqItem);
  }

  /**
   * Updates an existing FAQ item.
   *
   * @param id  the identifier of the FAQ item to update
   * @param dto data transfer object containing the updated FAQ item information
   * @return an {@link Optional} containing the updated FAQ item if the item exists,
   * or {@link Optional#empty()} if no item exists with the given identifier
   */
  public Optional<FaqItem> modifyOne(Long id, FaqItemUpdateDto dto) {
    Optional<FaqItem> existingFaqItem = faqItemRepository.findById(id);

    if (existingFaqItem.isEmpty()) {
      return Optional.empty();
    }

    FaqItem existing = existingFaqItem.get();
    existing.setQuestion(dto.getQuestion());
    existing.setAnswer(dto.getAnswer());

    FaqItem savedFaqItem = faqItemRepository.save(existing);
    return Optional.of(savedFaqItem);
  }

  /**
   * Deletes a FAQ item by its identifier.
   *
   * @param id the identifier of the FAQ item to delete
   * @return an {@link Optional} containing the deleted FAQ item if it existed,
   * or {@link Optional#empty()} if no item exists with the given identifier
   */
  public Optional<FaqItem> deleteOne(Long id) {
    Optional<FaqItem> faqItem = faqItemRepository.findById(id);

    if (faqItem.isPresent()) {
      faqItemRepository.delete(faqItem.get());
    }

    return faqItem;
  }

}
