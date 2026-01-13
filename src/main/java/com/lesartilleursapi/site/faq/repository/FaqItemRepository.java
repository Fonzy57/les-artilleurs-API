package com.lesartilleursapi.site.faq.repository;

import com.lesartilleursapi.site.faq.model.FaqItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link FaqItem} entities.
 * <p>
 * Provides standard CRUD operations for FAQ items through {@link JpaRepository}.
 * This repository is responsible for accessing and persisting FAQ-related data.
 */
@Repository
public interface FaqItemRepository extends JpaRepository<FaqItem, Long> {
}
