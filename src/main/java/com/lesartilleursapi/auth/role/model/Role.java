package com.lesartilleursapi.auth.role.model;

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
@Table(name = "roles")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 50)
  private String code;

  @Column(nullable = false, length = 100)
  private String label;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  private Instant createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private Instant updatedAt;

  // TODO  Relation inverse (optionnelle).
  // TODO À activer uniquement si un besoin métier apparaît
  // (ex: récupérer la liste des utilisateurs pour un rôle donné).
  // Pour l’instant, la relation est gérée uniquement côté User.
  //
  //  @OneToMany(mappedBy = "role")
  //  private List<User> users = new ArrayList<>();
}
