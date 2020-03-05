package com.carpooling.service.impl;

import com.carpooling.service.CarPoolingUserService;
import com.carpooling.domain.CarPoolingUser;
import com.carpooling.repository.CarPoolingUserRepository;
import com.carpooling.service.dto.CarPoolingUserDTO;
import com.carpooling.service.mapper.CarPoolingUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CarPoolingUser}.
 */
@Service
@Transactional
public class CarPoolingUserServiceImpl implements CarPoolingUserService {

    private final Logger log = LoggerFactory.getLogger(CarPoolingUserServiceImpl.class);

    private final CarPoolingUserRepository carPoolingUserRepository;

    private final CarPoolingUserMapper carPoolingUserMapper;

    public CarPoolingUserServiceImpl(CarPoolingUserRepository carPoolingUserRepository, CarPoolingUserMapper carPoolingUserMapper) {
        this.carPoolingUserRepository = carPoolingUserRepository;
        this.carPoolingUserMapper = carPoolingUserMapper;
    }

    /**
     * Save a carPoolingUser.
     *
     * @param carPoolingUserDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CarPoolingUserDTO save(CarPoolingUserDTO carPoolingUserDTO) {
        log.debug("Request to save CarPoolingUser : {}", carPoolingUserDTO);
        CarPoolingUser carPoolingUser = carPoolingUserMapper.toEntity(carPoolingUserDTO);
        carPoolingUser = carPoolingUserRepository.save(carPoolingUser);
        return carPoolingUserMapper.toDto(carPoolingUser);
    }

    /**
     * Get all the carPoolingUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CarPoolingUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CarPoolingUsers");
        return carPoolingUserRepository.findAll(pageable)
            .map(carPoolingUserMapper::toDto);
    }


    /**
     * Get one carPoolingUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CarPoolingUserDTO> findOne(Long id) {
        log.debug("Request to get CarPoolingUser : {}", id);
        return carPoolingUserRepository.findById(id)
            .map(carPoolingUserMapper::toDto);
    }

    /**
     * Delete the carPoolingUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CarPoolingUser : {}", id);
        carPoolingUserRepository.deleteById(id);
    }
}
