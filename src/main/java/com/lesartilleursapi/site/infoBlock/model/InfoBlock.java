package com.lesartilleursapi.site.infoBlock.model;

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
@Table(name = "info_block")
public class InfoBlock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private Integer slot;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  private Instant createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private Instant updatedAt;

}
