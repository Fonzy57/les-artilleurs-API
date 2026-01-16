package com.lesartilleursapi.site.clubInfo.model;

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
@Table(name = "club_info")
public class ClubInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonView(Views.Public.class)
  private Long id;

  @Column(nullable = false, length = 100)
  @JsonView(Views.Public.class)
  private String clubName;

  @Column(nullable = false, length = 100)
  @JsonView(Views.Public.class)
  private String stadiumName;

  @Column(nullable = false, length = 150)
  @JsonView(Views.Public.class)
  private String street;

  @Column(nullable = false, length = 80)
  @JsonView(Views.Public.class)
  private String city;

  @Column(nullable = false, length = 10)
  @JsonView(Views.Public.class)
  private String postalCode;

  @Column(nullable = false, length = 150)
  @JsonView(Views.Public.class)
  private String contactEmail;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  @JsonView(Views.Admin.class)
  private Instant createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  @JsonView(Views.Admin.class)
  private Instant updatedAt;
}
