package com.lesartilleursapi.site.infoBlock.service;

import com.lesartilleursapi.site.infoBlock.dto.InfoBlockCreateDto;
import com.lesartilleursapi.site.infoBlock.dto.InfoBlockUpdateDto;
import com.lesartilleursapi.site.infoBlock.model.InfoBlock;
import com.lesartilleursapi.site.infoBlock.repository.InfoBlockRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer responsible for managing {@link InfoBlock} entities.
 * <p>
 * This service handles business logic related to informational blocks displayed
 * on the public website, including creation, update, deletion, and retrieval.
 * <p>
 * Info blocks can optionally be assigned to a display slot. Only blocks with a
 * non-null slot are considered visible on the public website.
 */
@Service
public class InfoBlockService {

  private final InfoBlockRepository infoBlockRepository;

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Constructs a new {@link InfoBlockService}.
   *
   * @param infoBlockRepository repository used to access {@link InfoBlock} data
   */
  @Autowired
  public InfoBlockService(InfoBlockRepository infoBlockRepository) {
    this.infoBlockRepository = infoBlockRepository;
  }

  /**
   * Frees a display slot if it is already occupied by another InfoBlock.
   * <p>
   * If the slot is currently assigned to an InfoBlock different from the one
   * being created or updated, the slot is released by setting it to null.
   *
   * @param slot              the display slot to free
   * @param ignoreInfoBlockId the identifier of the InfoBlock to ignore
   *                          (used during update operations to avoid unassigning
   *                          the same InfoBlock)
   */
  private void freeSlotIfOccupied(Integer slot, Long ignoreInfoBlockId) {

    // 1. Look for an existing InfoBlock already using the given slot
    Optional<InfoBlock> optionalBlockWithSlot = infoBlockRepository.findBySlot(slot);

    // 2. If no InfoBlock uses this slot, nothing needs to be done
    if (optionalBlockWithSlot.isEmpty()) {
      return;
    }

    InfoBlock existingBlock = optionalBlockWithSlot.get();

    // 3. During an update operation, do not free the slot
    //    if it is already assigned to the same InfoBlock
    if (ignoreInfoBlockId != null) {
      if (existingBlock.getId().equals(ignoreInfoBlockId)) {
        return;
      }
    }

    // 4. The slot is occupied by another InfoBlock:
    //    release it by setting its slot to null
    existingBlock.setSlot(null);

    // 5. Persist the change in the database
    infoBlockRepository.save(existingBlock);

    // 6. Force immediate SQL execution to avoid UNIQUE constraint violation
    entityManager.flush();
  }

  /**
   * Retrieves all info blocks that are currently displayed on the public website.
   * <p>
   * Only info blocks with a non-null slot are returned. The result is ordered
   * by slot in ascending order.
   *
   * @return a list of displayed {@link InfoBlock} entities
   */
  public List<InfoBlock> getAllDisplayed() {
    return infoBlockRepository.findAllBySlotIsNotNullOrderBySlotAsc();
  }

  /**
   * Retrieves all info blocks, including both displayed and non-displayed ones.
   * <p>
   * This method is typically intended for administrative use.
   *
   * @return a list of all {@link InfoBlock} entities
   */
  public List<InfoBlock> getAll() {
    return infoBlockRepository.findAll();
  }

  /**
   * Retrieves a single info block by its identifier.
   *
   * @param id the identifier of the info block
   * @return an {@link Optional} containing the {@link InfoBlock} if found,
   * or {@link Optional#empty()} if no block exists with the given identifier
   */
  public Optional<InfoBlock> getOne(Long id) {
    return infoBlockRepository.findById(id);
  }

  /**
   * Creates a new {@link InfoBlock}.
   * <p>
   * The info block content is required, while the display slot is optional.
   * If a slot is provided, it is assigned to the new info block. If another
   * info block already occupies the same slot, it is automatically unassigned
   * to preserve slot uniqueness.
   * <p>
   * If no slot is specified, the info block is created as non-displayed
   * (slot set to {@code null}).
   *
   * @param dto data transfer object containing the info block creation data
   * @return the newly created {@link InfoBlock}
   */
  @Transactional
  public InfoBlock addOne(InfoBlockCreateDto dto) {
    // 1. Create a new InfoBlock entity
    //    The ID is explicitly set to null to ensure a creation operation
    InfoBlock infoBlock = new InfoBlock();
    infoBlock.setId(null);

    // 2. Set the content of the info block from the DTO
    infoBlock.setContent(dto.getContent());

    // Slot requested by the client (can be null)
    Integer targetSlot = dto.getSlot();

    // 3. If a display slot is requested
    if (targetSlot != null) {

      // 3.1 If another InfoBlock already occupies this slot,
      //     free it by setting its slot to null
      freeSlotIfOccupied(targetSlot, null);

      // 3.2 Assign the requested slot to the new InfoBlock
      infoBlock.setSlot(targetSlot);

    } else {
      // 4. No slot requested:
      //    the InfoBlock is created as non-displayed (slot = null)
      infoBlock.setSlot(null);
    }

    // 5. Persist the new InfoBlock in the database
    //    The transaction ensures that slot reassignments and creation
    //    are committed atomically
    return infoBlockRepository.save(infoBlock);
  }

  /**
   * Updates an existing {@link InfoBlock}.
   * <p>
   * This method updates the content of an info block and optionally modifies
   * its display slot. Slot management rules are applied to ensure that only
   * one info block can occupy a given slot at any time.
   * <p>
   * Slot behavior:
   * <ul>
   *   <li>If the slot is unchanged (null → null or same value), only the content is updated.</li>
   *   <li>If the slot is removed (value → null), the info block becomes non-displayed.</li>
   *   <li>If a slot is assigned or changed, the previous occupant of that slot
   *       (if any) is automatically unassigned.</li>
   * </ul>
   *
   * @param id  the identifier of the info block to update
   * @param dto data transfer object containing the updated info block data
   * @return an {@link Optional} containing the updated {@link InfoBlock} if it exists,
   * or {@link Optional#empty()} if no info block exists with the given identifier
   */
  @Transactional
  public Optional<InfoBlock> updateOne(Long id, InfoBlockUpdateDto dto) {
    // 1. Check if the InfoBlock exists
    Optional<InfoBlock> existingInfoBlock = infoBlockRepository.findById(id);

    if (existingInfoBlock.isEmpty()) {
      return Optional.empty();
    }

    InfoBlock existing = existingInfoBlock.get();

    // 2. Update content (independent from slot logic)
    existing.setContent(dto.getContent());

    Integer currentSlot = existing.getSlot(); // current slot in database
    Integer targetSlot = dto.getSlot(); // slot requested by the client

    // Slot state evaluation
    boolean slotUnchanged =
        (currentSlot == null && targetSlot == null) || (currentSlot != null && currentSlot.equals(targetSlot));
    boolean removeSlot = currentSlot != null && targetSlot == null;

    // 3. Slot unchanged (null -> null OR same value)
    if (slotUnchanged) {
      return Optional.of(infoBlockRepository.save(existing));
    }

    // 4. Slot removed (value -> null)
    if (removeSlot) {
      existing.setSlot(null);
      return Optional.of(infoBlockRepository.save(existing));
    }

    // 5. Slot assigned or changed (null -> value OR value -> other value)
    // At this point, targetSlot is necessarily non-null and different from currentSlot
    freeSlotIfOccupied(targetSlot, id);
    existing.setSlot(targetSlot);

    return Optional.of(infoBlockRepository.save(existing));
  }

  /**
   * Deletes an {@link InfoBlock} by its identifier.
   * <p>
   * If an info block with the given ID exists, it is deleted and returned.
   * Otherwise, no deletion is performed and an empty {@link Optional} is returned.
   *
   * @param id the identifier of the info block to delete
   * @return an {@link Optional} containing the deleted {@link InfoBlock} if it existed,
   * or {@link Optional#empty()} if no info block exists with the given identifier
   */
  public Optional<InfoBlock> deleteOne(Long id) {
    Optional<InfoBlock> infoBlock = infoBlockRepository.findById(id);

    if (infoBlock.isPresent()) {
      infoBlockRepository.delete(infoBlock.get());
    }

    return infoBlock;
  }
}
