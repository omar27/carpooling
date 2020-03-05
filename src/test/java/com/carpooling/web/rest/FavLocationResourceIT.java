package com.carpooling.web.rest;

import com.carpooling.CarpoolingApp;
import com.carpooling.domain.FavLocation;
import com.carpooling.domain.CarPoolingUser;
import com.carpooling.domain.Location;
import com.carpooling.repository.FavLocationRepository;
import com.carpooling.service.FavLocationService;
import com.carpooling.service.dto.FavLocationDTO;
import com.carpooling.service.mapper.FavLocationMapper;
import com.carpooling.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.carpooling.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FavLocationResource} REST controller.
 */
@SpringBootTest(classes = CarpoolingApp.class)
public class FavLocationResourceIT {

    @Autowired
    private FavLocationRepository favLocationRepository;

    @Autowired
    private FavLocationMapper favLocationMapper;

    @Autowired
    private FavLocationService favLocationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restFavLocationMockMvc;

    private FavLocation favLocation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FavLocationResource favLocationResource = new FavLocationResource(favLocationService);
        this.restFavLocationMockMvc = MockMvcBuilders.standaloneSetup(favLocationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FavLocation createEntity(EntityManager em) {
        FavLocation favLocation = new FavLocation();
        // Add required entity
        CarPoolingUser carPoolingUser;
        if (TestUtil.findAll(em, CarPoolingUser.class).isEmpty()) {
            carPoolingUser = CarPoolingUserResourceIT.createEntity(em);
            em.persist(carPoolingUser);
            em.flush();
        } else {
            carPoolingUser = TestUtil.findAll(em, CarPoolingUser.class).get(0);
        }
        favLocation.setUser(carPoolingUser);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        favLocation.setDestination(location);
        return favLocation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FavLocation createUpdatedEntity(EntityManager em) {
        FavLocation favLocation = new FavLocation();
        // Add required entity
        CarPoolingUser carPoolingUser;
        if (TestUtil.findAll(em, CarPoolingUser.class).isEmpty()) {
            carPoolingUser = CarPoolingUserResourceIT.createUpdatedEntity(em);
            em.persist(carPoolingUser);
            em.flush();
        } else {
            carPoolingUser = TestUtil.findAll(em, CarPoolingUser.class).get(0);
        }
        favLocation.setUser(carPoolingUser);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        favLocation.setDestination(location);
        return favLocation;
    }

    @BeforeEach
    public void initTest() {
        favLocation = createEntity(em);
    }

    @Test
    @Transactional
    public void createFavLocation() throws Exception {
        int databaseSizeBeforeCreate = favLocationRepository.findAll().size();

        // Create the FavLocation
        FavLocationDTO favLocationDTO = favLocationMapper.toDto(favLocation);
        restFavLocationMockMvc.perform(post("/api/fav-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favLocationDTO)))
            .andExpect(status().isCreated());

        // Validate the FavLocation in the database
        List<FavLocation> favLocationList = favLocationRepository.findAll();
        assertThat(favLocationList).hasSize(databaseSizeBeforeCreate + 1);
        FavLocation testFavLocation = favLocationList.get(favLocationList.size() - 1);
    }

    @Test
    @Transactional
    public void createFavLocationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = favLocationRepository.findAll().size();

        // Create the FavLocation with an existing ID
        favLocation.setId(1L);
        FavLocationDTO favLocationDTO = favLocationMapper.toDto(favLocation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavLocationMockMvc.perform(post("/api/fav-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favLocationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FavLocation in the database
        List<FavLocation> favLocationList = favLocationRepository.findAll();
        assertThat(favLocationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFavLocations() throws Exception {
        // Initialize the database
        favLocationRepository.saveAndFlush(favLocation);

        // Get all the favLocationList
        restFavLocationMockMvc.perform(get("/api/fav-locations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favLocation.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getFavLocation() throws Exception {
        // Initialize the database
        favLocationRepository.saveAndFlush(favLocation);

        // Get the favLocation
        restFavLocationMockMvc.perform(get("/api/fav-locations/{id}", favLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(favLocation.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFavLocation() throws Exception {
        // Get the favLocation
        restFavLocationMockMvc.perform(get("/api/fav-locations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavLocation() throws Exception {
        // Initialize the database
        favLocationRepository.saveAndFlush(favLocation);

        int databaseSizeBeforeUpdate = favLocationRepository.findAll().size();

        // Update the favLocation
        FavLocation updatedFavLocation = favLocationRepository.findById(favLocation.getId()).get();
        // Disconnect from session so that the updates on updatedFavLocation are not directly saved in db
        em.detach(updatedFavLocation);
        FavLocationDTO favLocationDTO = favLocationMapper.toDto(updatedFavLocation);

        restFavLocationMockMvc.perform(put("/api/fav-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favLocationDTO)))
            .andExpect(status().isOk());

        // Validate the FavLocation in the database
        List<FavLocation> favLocationList = favLocationRepository.findAll();
        assertThat(favLocationList).hasSize(databaseSizeBeforeUpdate);
        FavLocation testFavLocation = favLocationList.get(favLocationList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingFavLocation() throws Exception {
        int databaseSizeBeforeUpdate = favLocationRepository.findAll().size();

        // Create the FavLocation
        FavLocationDTO favLocationDTO = favLocationMapper.toDto(favLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavLocationMockMvc.perform(put("/api/fav-locations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favLocationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FavLocation in the database
        List<FavLocation> favLocationList = favLocationRepository.findAll();
        assertThat(favLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFavLocation() throws Exception {
        // Initialize the database
        favLocationRepository.saveAndFlush(favLocation);

        int databaseSizeBeforeDelete = favLocationRepository.findAll().size();

        // Delete the favLocation
        restFavLocationMockMvc.perform(delete("/api/fav-locations/{id}", favLocation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FavLocation> favLocationList = favLocationRepository.findAll();
        assertThat(favLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavLocation.class);
        FavLocation favLocation1 = new FavLocation();
        favLocation1.setId(1L);
        FavLocation favLocation2 = new FavLocation();
        favLocation2.setId(favLocation1.getId());
        assertThat(favLocation1).isEqualTo(favLocation2);
        favLocation2.setId(2L);
        assertThat(favLocation1).isNotEqualTo(favLocation2);
        favLocation1.setId(null);
        assertThat(favLocation1).isNotEqualTo(favLocation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavLocationDTO.class);
        FavLocationDTO favLocationDTO1 = new FavLocationDTO();
        favLocationDTO1.setId(1L);
        FavLocationDTO favLocationDTO2 = new FavLocationDTO();
        assertThat(favLocationDTO1).isNotEqualTo(favLocationDTO2);
        favLocationDTO2.setId(favLocationDTO1.getId());
        assertThat(favLocationDTO1).isEqualTo(favLocationDTO2);
        favLocationDTO2.setId(2L);
        assertThat(favLocationDTO1).isNotEqualTo(favLocationDTO2);
        favLocationDTO1.setId(null);
        assertThat(favLocationDTO1).isNotEqualTo(favLocationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(favLocationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(favLocationMapper.fromId(null)).isNull();
    }
}
