package com.lesartilleursapi.site.infoBlock.service;

import com.lesartilleursapi.site.infoBlock.dto.InfoBlockCreateDto;
import com.lesartilleursapi.site.infoBlock.dto.InfoBlockUpdateDto;
import com.lesartilleursapi.site.infoBlock.model.InfoBlock;
import com.lesartilleursapi.site.infoBlock.repository.InfoBlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
   * Creates a new info block.
   * <p>
   * The newly created block may optionally be assigned to a display slot.
   *
   * @param dto data transfer object containing the info block creation data
   * @return the created {@link InfoBlock}
   */
  public InfoBlock addOne(InfoBlockCreateDto dto) {
    InfoBlock infoBlock = new InfoBlock();
    infoBlock.setId(null);
    infoBlock.setSlot(dto.getSlot());
    infoBlock.setContent(dto.getContent());

    return infoBlockRepository.save(infoBlock);
  }

  /**
   * Updates an existing info block.
   *
   * @param id  the identifier of the info block to update
   * @param dto data transfer object containing the updated info block data
   * @return an {@link Optional} containing the updated {@link InfoBlock} if the block exists,
   * or {@link Optional#empty()} if no block exists with the given identifier
   */
  public Optional<InfoBlock> modifyOne(Long id, InfoBlockUpdateDto dto) {
    Optional<InfoBlock> existingInfoBlock = infoBlockRepository.findById(id);

    if (existingInfoBlock.isEmpty()) {
      return Optional.empty();
    }

    InfoBlock existing = existingInfoBlock.get();
    existing.setSlot(dto.getSlot());
    existing.setContent(dto.getContent());

    InfoBlock savedInfoBlock = infoBlockRepository.save(existing);

    return Optional.of(savedInfoBlock);
  }

  /**
   * Deletes an info block by its identifier.
   *
   * @param id the identifier of the info block to delete
   * @return {@code true} if the block was deleted successfully,
   * {@code false} if no block exists with the given identifier
   */
  public boolean deleteOne(Long id) {
    if (!infoBlockRepository.existsById(id)) {
      return false;
    }

    infoBlockRepository.deleteById(id);
    return true;
  }
}
