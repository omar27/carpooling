package com.carpooling.service.impl;

import com.carpooling.service.FavLocationService;
import com.carpooling.domain.FavLocation;
import com.carpooling.repository.FavLocationRepository;
import com.carpooling.service.dto.FavLocationDTO;
import com.carpooling.service.mapper.FavLocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link FavLocation}.
 */
@Service
@Transactional
public class FavLocationServiceImpl implements FavLocationService {

    private final Logger log = LoggerFactory.getLogger(FavLocationServiceImpl.class);

    private final FavLocationRepository favLocationRepository;

    private final FavLocationMapper favLocationMapper;

    public FavLocationServiceImpl(FavLocationRepository favLocationRepository, FavLocationMapper favLocationMapper) {
        this.favLocationRepository = favLocationRepository;
        this.favLocationMapper = favLocationMapper;
    }

    /**
     * Save a favLocation.
     *
     * @param favLocationDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FavLocationDTO save(FavLocationDTO favLocationDTO) {
        log.debug("Request to save FavLocation : {}", favLocationDTO);
        FavLocation favLocation = favLocationMapper.toEntity(favLocationDTO);
        favLocation = favLocationRepository.save(favLocation);
        return favLocationMapper.toDto(favLocation);
    }

    /**
     * Get all the favLocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FavLocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FavLocations");
        return favLocationRepository.findAll(pageable)
            .map(favLocationMapper::toDto);
    }


    /**
     * Get one favLocation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FavLocationDTO> findOne(Long id) {
        log.debug("Request to get FavLocation : {}", id);
        return favLocationRepository.findById(id)
            .map(favLocationMapper::toDto);
    }

    /**
     * Delete the favLocation by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FavLocation : {}", id);
        favLocationRepository.deleteById(id);
    }
}
