package com.carpooling.web.rest;

import com.carpooling.service.CarPoolingUserService;
import com.carpooling.web.rest.errors.BadRequestAlertException;
import com.carpooling.service.dto.CarPoolingUserDTO;

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
 * REST controller for managing {@link com.carpooling.domain.CarPoolingUser}.
 */
@RestController
@RequestMapping("/api")
public class CarPoolingUserResource {

    private final Logger log = LoggerFactory.getLogger(CarPoolingUserResource.class);

    private static final String ENTITY_NAME = "carPoolingUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarPoolingUserService carPoolingUserService;

    public CarPoolingUserResource(CarPoolingUserService carPoolingUserService) {
        this.carPoolingUserService = carPoolingUserService;
    }

    /**
     * {@code POST  /car-pooling-users} : Create a new carPoolingUser.
     *
     * @param carPoolingUserDTO the carPoolingUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carPoolingUserDTO, or with status {@code 400 (Bad Request)} if the carPoolingUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/car-pooling-users")
    public ResponseEntity<CarPoolingUserDTO> createCarPoolingUser(@Valid @RequestBody CarPoolingUserDTO carPoolingUserDTO) throws URISyntaxException {
        log.debug("REST request to save CarPoolingUser : {}", carPoolingUserDTO);
        if (carPoolingUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new carPoolingUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CarPoolingUserDTO result = carPoolingUserService.save(carPoolingUserDTO);
        return ResponseEntity.created(new URI("/api/car-pooling-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /car-pooling-users} : Updates an existing carPoolingUser.
     *
     * @param carPoolingUserDTO the carPoolingUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carPoolingUserDTO,
     * or with status {@code 400 (Bad Request)} if the carPoolingUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carPoolingUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/car-pooling-users")
    public ResponseEntity<CarPoolingUserDTO> updateCarPoolingUser(@Valid @RequestBody CarPoolingUserDTO carPoolingUserDTO) throws URISyntaxException {
        log.debug("REST request to update CarPoolingUser : {}", carPoolingUserDTO);
        if (carPoolingUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CarPoolingUserDTO result = carPoolingUserService.save(carPoolingUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, carPoolingUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /car-pooling-users} : get all the carPoolingUsers.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carPoolingUsers in body.
     */
    @GetMapping("/car-pooling-users")
    public ResponseEntity<List<CarPoolingUserDTO>> getAllCarPoolingUsers(Pageable pageable) {
        log.debug("REST request to get a page of CarPoolingUsers");
        Page<CarPoolingUserDTO> page = carPoolingUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /car-pooling-users/:id} : get the "id" carPoolingUser.
     *
     * @param id the id of the carPoolingUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carPoolingUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/car-pooling-users/{id}")
    public ResponseEntity<CarPoolingUserDTO> getCarPoolingUser(@PathVariable Long id) {
        log.debug("REST request to get CarPoolingUser : {}", id);
        Optional<CarPoolingUserDTO> carPoolingUserDTO = carPoolingUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carPoolingUserDTO);
    }

    /**
     * {@code DELETE  /car-pooling-users/:id} : delete the "id" carPoolingUser.
     *
     * @param id the id of the carPoolingUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/car-pooling-users/{id}")
    public ResponseEntity<Void> deleteCarPoolingUser(@PathVariable Long id) {
        log.debug("REST request to delete CarPoolingUser : {}", id);
        carPoolingUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
