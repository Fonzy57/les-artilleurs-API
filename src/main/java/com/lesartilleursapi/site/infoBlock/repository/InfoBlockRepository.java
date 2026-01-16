package com.lesartilleursapi.site.infoBlock.repository;

import com.lesartilleursapi.site.infoBlock.model.InfoBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link InfoBlock} entities.
 * <p>
 * Provides standard CRUD operations through {@link JpaRepository} as well as
 * custom query methods related to info block visibility and display order.
 */
@Repository
public interface InfoBlockRepository extends JpaRepository<InfoBlock, Long> {

  /**
   * Retrieves all info blocks that are assigned to a display slot.
   * <p>
   * Only blocks with a non-null slot are returned, ordered by slot in ascending order.
   * This method is typically used to fetch the info blocks displayed
   * on the public website.
   *
   * @return a list of displayed {@link InfoBlock} entities ordered by slot
   */
  List<InfoBlock> findAllBySlotIsNotNullOrderBySlotAsc();

  /**
   * Retrieves the info block currently assigned to the given display slot.
   * <p>
   * Since display slots are unique, this method returns at most one {@link InfoBlock}.
   * It is primarily used internally to enforce slot uniqueness when creating
   * or updating info blocks.
   *
   * @param slot the display slot to search for
   * @return an {@link Optional} containing the {@link InfoBlock} assigned to the slot,
   * or {@link Optional#empty()} if the slot is not currently in use
   */
  Optional<InfoBlock> findBySlot(Integer slot);
}
