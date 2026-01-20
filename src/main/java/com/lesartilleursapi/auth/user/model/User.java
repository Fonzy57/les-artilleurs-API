package com.lesartilleursapi.auth.user.model;

import com.lesartilleursapi.auth.role.model.Role;
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
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String firstname;

  @Column(nullable = false, length = 100)
  private String lastname;

  @Column(nullable = false, unique = true, length = 150)
  private String email;

  @Column(nullable = false)
  private String passwordHash;

  @Column
  private Instant lastLoginAt;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  private Instant createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private Instant updatedAt;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "role_id", nullable = false)
  private Role role;
}
