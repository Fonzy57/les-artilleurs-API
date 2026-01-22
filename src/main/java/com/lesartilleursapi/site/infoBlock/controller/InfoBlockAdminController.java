package com.lesartilleursapi.site.infoBlock.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.lesartilleursapi.jsonview.Views;
import com.lesartilleursapi.site.infoBlock.dto.InfoBlockCreateDto;
import com.lesartilleursapi.site.infoBlock.dto.InfoBlockUpdateDto;
import com.lesartilleursapi.site.infoBlock.model.InfoBlock;
import com.lesartilleursapi.site.infoBlock.service.InfoBlockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//  TODO METTRE ANNOTATION POUR PROTÉGER PAR ADMIN

/**
 * Administrative REST controller for managing {@link InfoBlock} entities.
 * <p>
 * This controller exposes CRUD endpoints intended for administrative use.
 * It allows administrators to create, update, delete, and retrieve info blocks,
 * including those not currently displayed on the public website.
 * <p>
 * Responses are serialized using the {@link Views.Admin} JSON view.
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/info-block")
public class InfoBlockAdminController {

  private final InfoBlockService infoBlockService;

  /**
   * Constructs a new {@link InfoBlockAdminController}.
   *
   * @param infoBlockService service responsible for {@link InfoBlock} business logic
   */
  @Autowired
  public InfoBlockAdminController(InfoBlockService infoBlockService) {
    this.infoBlockService = infoBlockService;
  }

  /**
   * Retrieves all info blocks, including both displayed (slot assigned)
   * and non-displayed (slot {@code null}) ones.
   *
   * @return a list of all {@link InfoBlock} entities
   */
  @GetMapping
  @JsonView(Views.Admin.class)
  public List<InfoBlock> getAllInfoBlocks() {
    return infoBlockService.getAll();
  }

  /**
   * Retrieves a single info block by its identifier.
   *
   * @param id the identifier of the info block
   * @return {@code 200 OK} with the requested {@link InfoBlock} if found,
   * or {@code 404 Not Found} if no info block exists with the given identifier
   */
  @GetMapping("/{id}")
  @JsonView(Views.Admin.class)
  public ResponseEntity<InfoBlock> getOneInfoBlock(@PathVariable Long id) {
    Optional<InfoBlock> infoBlock = infoBlockService.getOne(id);

    if (infoBlock.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(infoBlock.get());
  }

  /**
   * Creates a new info block.
   * <p>
   * The content is required. The slot is optional; if provided, it will be assigned
   * according to the service slot management rules (slot uniqueness is enforced).
   *
   * @param dto the data transfer object containing creation data
   * @return {@code 201 Created} with the created {@link InfoBlock}
   */
  @PostMapping
  @JsonView(Views.Admin.class)
  public ResponseEntity<InfoBlock> addOneInfoBlock(@RequestBody @Valid InfoBlockCreateDto dto) {
    InfoBlock created = infoBlockService.addOne(dto);
    return ResponseEntity.status(201).body(created);
  }

  /**
   * Updates an existing info block.
   * <p>
   * Updates content and optionally updates the slot (including removing it).
   * Slot uniqueness is enforced by the service layer.
   *
   * @param id  the identifier of the info block to update
   * @param dto the data transfer object containing updated data
   * @return {@code 200 OK} with the updated {@link InfoBlock} if found,
   * or {@code 404 Not Found} if no info block exists with the given identifier
   */
  @PutMapping("/{id}")
  @JsonView(Views.Admin.class)
  public ResponseEntity<InfoBlock> updateOneInfoBlock(@PathVariable Long id,
      @RequestBody @Valid InfoBlockUpdateDto dto) {
    Optional<InfoBlock> existingInfoBlock = infoBlockService.updateOne(id, dto);

    if (existingInfoBlock.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(existingInfoBlock.get());
  }

  /**
   * Deletes an info block by its identifier.
   *
   * @param id the identifier of the info block to delete
   * @return {@code 204 No Content} if the info block was deleted,
   * or {@code 404 Not Found} if no info block exists with the given identifier
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOneInfoBlock(@PathVariable Long id) {
    Optional<InfoBlock> deletedInfoBLock = infoBlockService.deleteOne(id);

    if (deletedInfoBLock.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.noContent().build();
  }
}
