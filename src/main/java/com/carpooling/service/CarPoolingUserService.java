package com.carpooling.service;

import com.carpooling.service.dto.CarPoolingUserDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.carpooling.domain.CarPoolingUser}.
 */
public interface CarPoolingUserService {

    /**
     * Save a carPoolingUser.
     *
     * @param carPoolingUserDTO the entity to save.
     * @return the persisted entity.
     */
    CarPoolingUserDTO save(CarPoolingUserDTO carPoolingUserDTO);

    /**
     * Get all the carPoolingUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CarPoolingUserDTO> findAll(Pageable pageable);


    /**
     * Get the "id" carPoolingUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CarPoolingUserDTO> findOne(Long id);

    /**
     * Delete the "id" carPoolingUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
