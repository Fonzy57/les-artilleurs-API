package com.lesartilleursapi.auth.user.repository;

import com.lesartilleursapi.auth.user.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @EntityGraph(attributePaths = "role")
  Optional<User> findByEmail(String email);

  @EntityGraph(attributePaths = "role")
  @Query("SELECT u FROM User u")
  List<User> findAllWithRole();

  @EntityGraph(attributePaths = "role")
  Optional<User> findOneById(Long id);

  boolean existsByEmail(String email);

  /**
   * Checks whether an email address is already used by another user.
   * <p>
   * This method is typically used during an update operation to ensure
   * that the email remains unique, excluding the current user identified by the given id.
   *
   * @param email the email address to check
   * @param id    the id of the user to exclude from the check
   * @return true if another user with the same email exists, false otherwise
   */
  boolean existsByEmailAndIdNot(String email, Long id);

}
