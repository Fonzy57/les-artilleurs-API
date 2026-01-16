package com.lesartilleursapi.site.infoBlock.model;

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
@Table(name = "info_block")
public class InfoBlock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView(Views.Public.class)
  private Long id;

  @Column(unique = true)
  @JsonView(Views.Admin.class)
  private Integer slot;

  @Column(nullable = false)
  @JsonView(Views.Public.class)
  private String content;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  @JsonView(Views.Admin.class)
  private Instant createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  @JsonView(Views.Admin.class)
  private Instant updatedAt;

}
