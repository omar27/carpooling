package com.carpooling.service;

import com.carpooling.service.dto.FavLocationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.carpooling.domain.FavLocation}.
 */
public interface FavLocationService {

    /**
     * Save a favLocation.
     *
     * @param favLocationDTO the entity to save.
     * @return the persisted entity.
     */
    FavLocationDTO save(FavLocationDTO favLocationDTO);

    /**
     * Get all the favLocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FavLocationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" favLocation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FavLocationDTO> findOne(Long id);

    /**
     * Delete the "id" favLocation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
