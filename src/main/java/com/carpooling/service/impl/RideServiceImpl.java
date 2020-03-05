package com.carpooling.service.impl;

import com.carpooling.service.RideService;
import com.carpooling.domain.CarPoolingUser;
import com.carpooling.domain.FavLocation;
import com.carpooling.domain.Location;
import com.carpooling.domain.Ride;
import com.carpooling.repository.RideRepository;
import com.carpooling.security.SecurityUtils;
import com.carpooling.service.dto.RideDTO;
import com.carpooling.service.mapper.RideMapper;
import com.carpooling.web.websocket.ActivityService;

import org.jboss.logging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Ride}.
 */
@Service
@Transactional
public class RideServiceImpl implements RideService {

    private final Logger log = LoggerFactory.getLogger(RideServiceImpl.class);

    private final RideRepository rideRepository;

    private final RideMapper rideMapper;

    private final ActivityService activityService;


    public RideServiceImpl(ActivityService activityService, RideRepository rideRepository, RideMapper rideMapper) {
        this.rideRepository = rideRepository;
        this.rideMapper = rideMapper;
        this.activityService = activityService;
    }

    /**
     * Save a ride.
     *
     * @param rideDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public RideDTO save(RideDTO rideDTO) {
        log.debug("Request to save Ride : {}", rideDTO);
        Ride ride = rideMapper.toEntity(rideDTO);
        ride = rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    /**
     * Get all the rides.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RideDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rides");
        return rideRepository.findAll(pageable).map(rideMapper::toDto);
    }

    /**
     * Get one ride by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RideDTO> findOne(Long id) {
        log.debug("Request to get Ride : {}", id);
        return rideRepository.findById(id).map(rideMapper::toDto);
    }

    /**
     * Delete the ride by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ride : {}", id);
        rideRepository.deleteById(id);
    }

   
}
