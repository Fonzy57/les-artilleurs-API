package com.lesartilleursapi.site.infoBlock.repository;

import com.lesartilleursapi.site.infoBlock.model.InfoBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
