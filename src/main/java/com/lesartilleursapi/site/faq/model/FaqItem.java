package com.lesartilleursapi.site.faq.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.lesartilleursapi.jsonview.Views;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "faq_item")
public class FaqItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView(Views.Public.class)
  private Long id;

  @Column(nullable = false)
  @JsonView(Views.Public.class)
  private String question;

  @Column(nullable = false, columnDefinition = "TEXT")
  @JsonView(Views.Public.class)
  private String answer;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  @JsonView(Views.Admin.class)
  private Instant createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  @JsonView(Views.Admin.class)
  private Instant updatedAt;
}
