package com.carpooling.web.rest;

import com.carpooling.service.RideService;
import com.carpooling.web.rest.errors.BadRequestAlertException;
import com.carpooling.service.dto.RideDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.carpooling.domain.Ride}.
 */
@RestController
@RequestMapping("/api")
public class RideResource {

    private final Logger log = LoggerFactory.getLogger(RideResource.class);

    private static final String ENTITY_NAME = "ride";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RideService rideService;

    public RideResource(RideService rideService) {
        this.rideService = rideService;
    }

    /**
     * {@code POST  /rides} : Create a new ride.
     *
     * @param rideDTO the rideDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rideDTO, or with status {@code 400 (Bad Request)} if the ride has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rides")
    public ResponseEntity<RideDTO> createRide(@Valid @RequestBody RideDTO rideDTO) throws URISyntaxException {
        log.debug("REST request to save Ride : {}", rideDTO);
        if (rideDTO.getId() != null) {
            throw new BadRequestAlertException("A new ride cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RideDTO result = rideService.save(rideDTO);
        return ResponseEntity.created(new URI("/api/rides/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rides} : Updates an existing ride.
     *
     * @param rideDTO the rideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rideDTO,
     * or with status {@code 400 (Bad Request)} if the rideDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rides")
    public ResponseEntity<RideDTO> updateRide(@Valid @RequestBody RideDTO rideDTO) throws URISyntaxException {
        log.debug("REST request to update Ride : {}", rideDTO);
        if (rideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RideDTO result = rideService.save(rideDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rideDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rides} : get all the rides.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rides in body.
     */
    @GetMapping("/rides")
    public ResponseEntity<List<RideDTO>> getAllRides(Pageable pageable) {
        log.debug("REST request to get a page of Rides");
        Page<RideDTO> page = rideService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rides/:id} : get the "id" ride.
     *
     * @param id the id of the rideDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rideDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rides/{id}")
    public ResponseEntity<RideDTO> getRide(@PathVariable Long id) {
        log.debug("REST request to get Ride : {}", id);
        Optional<RideDTO> rideDTO = rideService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rideDTO);
    }

    /**
     * {@code DELETE  /rides/:id} : delete the "id" ride.
     *
     * @param id the id of the rideDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rides/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Long id) {
        log.debug("REST request to delete Ride : {}", id);
        rideService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
