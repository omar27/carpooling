package com.carpooling.web.rest;

import com.carpooling.service.FavLocationService;
import com.carpooling.web.rest.errors.BadRequestAlertException;
import com.carpooling.service.dto.FavLocationDTO;

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
 * REST controller for managing {@link com.carpooling.domain.FavLocation}.
 */
@RestController
@RequestMapping("/api")
public class FavLocationResource {

    private final Logger log = LoggerFactory.getLogger(FavLocationResource.class);

    private static final String ENTITY_NAME = "favLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FavLocationService favLocationService;

    public FavLocationResource(FavLocationService favLocationService) {
        this.favLocationService = favLocationService;
    }

    /**
     * {@code POST  /fav-locations} : Create a new favLocation.
     *
     * @param favLocationDTO the favLocationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new favLocationDTO, or with status {@code 400 (Bad Request)} if the favLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fav-locations")
    public ResponseEntity<FavLocationDTO> createFavLocation(@Valid @RequestBody FavLocationDTO favLocationDTO) throws URISyntaxException {
        log.debug("REST request to save FavLocation : {}", favLocationDTO);
        if (favLocationDTO.getId() != null) {
            throw new BadRequestAlertException("A new favLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FavLocationDTO result = favLocationService.save(favLocationDTO);
        return ResponseEntity.created(new URI("/api/fav-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fav-locations} : Updates an existing favLocation.
     *
     * @param favLocationDTO the favLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favLocationDTO,
     * or with status {@code 400 (Bad Request)} if the favLocationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the favLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fav-locations")
    public ResponseEntity<FavLocationDTO> updateFavLocation(@Valid @RequestBody FavLocationDTO favLocationDTO) throws URISyntaxException {
        log.debug("REST request to update FavLocation : {}", favLocationDTO);
        if (favLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FavLocationDTO result = favLocationService.save(favLocationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, favLocationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fav-locations} : get all the favLocations.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of favLocations in body.
     */
    @GetMapping("/fav-locations")
    public ResponseEntity<List<FavLocationDTO>> getAllFavLocations(Pageable pageable) {
        log.debug("REST request to get a page of FavLocations");
        Page<FavLocationDTO> page = favLocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fav-locations/:id} : get the "id" favLocation.
     *
     * @param id the id of the favLocationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the favLocationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fav-locations/{id}")
    public ResponseEntity<FavLocationDTO> getFavLocation(@PathVariable Long id) {
        log.debug("REST request to get FavLocation : {}", id);
        Optional<FavLocationDTO> favLocationDTO = favLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(favLocationDTO);
    }

    /**
     * {@code DELETE  /fav-locations/:id} : delete the "id" favLocation.
     *
     * @param id the id of the favLocationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fav-locations/{id}")
    public ResponseEntity<Void> deleteFavLocation(@PathVariable Long id) {
        log.debug("REST request to delete FavLocation : {}", id);
        favLocationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
