package com.lesartilleursapi.site.faq.repository;

import com.lesartilleursapi.site.faq.model.FaqItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqItemRepository extends JpaRepository<FaqItem, Long> {
}
