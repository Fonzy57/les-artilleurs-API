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
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "club_info")
public class ClubInfo {

  private static final long SINGLETON_ID = 1L;

  protected ClubInfo() {
  }

  @Id
  @JsonView(Views.Public.class)
  private Long id = SINGLETON_ID;

  @Setter
  @Column(nullable = false, length = 100)
  @JsonView(Views.Public.class)
  private String clubName;

  @Setter
  @Column(nullable = false, length = 100)
  @JsonView(Views.Public.class)
  private String stadiumName;

  @Setter
  @Column(nullable = false, length = 150)
  @JsonView(Views.Public.class)
  private String street;

  @Setter
  @Column(nullable = false, length = 80)
  @JsonView(Views.Public.class)
  private String city;

  @Setter
  @Column(nullable = false, length = 10)
  @JsonView(Views.Public.class)
  private String postalCode;

  @Setter
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

  public static long getClubInfoId() {
    return SINGLETON_ID;
  }

  @PrePersist
  @PreUpdate
  private void enforceSingletonId() {
    if (id == null) {
      id = SINGLETON_ID;
    }
    if (!id.equals(SINGLETON_ID)) {
      throw new IllegalStateException("ClubInfo must have id=1");
    }
  }

}
