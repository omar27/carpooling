package com.carpooling.web.rest;

import com.carpooling.CarpoolingApp;
import com.carpooling.domain.Ride;
import com.carpooling.domain.CarPoolingUser;
import com.carpooling.domain.Location;
import com.carpooling.repository.RideRepository;
import com.carpooling.service.RideService;
import com.carpooling.service.dto.RideDTO;
import com.carpooling.service.mapper.RideMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.carpooling.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.carpooling.domain.enumeration.Status;
/**
 * Integration tests for the {@link RideResource} REST controller.
 */
@SpringBootTest(classes = CarpoolingApp.class)
public class RideResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_STATUS = Status.SCHEDULED;
    private static final Status UPDATED_STATUS = Status.BOOKED;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RideMapper rideMapper;

    @Autowired
    private RideService rideService;

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

    private MockMvc restRideMockMvc;

    private Ride ride;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RideResource rideResource = new RideResource(rideService);
        this.restRideMockMvc = MockMvcBuilders.standaloneSetup(rideResource)
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
    public static Ride createEntity(EntityManager em) {
        Ride ride = new Ride()
            .date(DEFAULT_DATE)
            .status(DEFAULT_STATUS);
        // Add required entity
        CarPoolingUser carPoolingUser;
        if (TestUtil.findAll(em, CarPoolingUser.class).isEmpty()) {
            carPoolingUser = CarPoolingUserResourceIT.createEntity(em);
            em.persist(carPoolingUser);
            em.flush();
        } else {
            carPoolingUser = TestUtil.findAll(em, CarPoolingUser.class).get(0);
        }
        ride.setDriver(carPoolingUser);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        ride.setSource(location);
        // Add required entity
        ride.setDestination(location);
        return ride;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ride createUpdatedEntity(EntityManager em) {
        Ride ride = new Ride()
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);
        // Add required entity
        CarPoolingUser carPoolingUser;
        if (TestUtil.findAll(em, CarPoolingUser.class).isEmpty()) {
            carPoolingUser = CarPoolingUserResourceIT.createUpdatedEntity(em);
            em.persist(carPoolingUser);
            em.flush();
        } else {
            carPoolingUser = TestUtil.findAll(em, CarPoolingUser.class).get(0);
        }
        ride.setDriver(carPoolingUser);
        // Add required entity
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            location = LocationResourceIT.createUpdatedEntity(em);
            em.persist(location);
            em.flush();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        ride.setSource(location);
        // Add required entity
        ride.setDestination(location);
        return ride;
    }

    @BeforeEach
    public void initTest() {
        ride = createEntity(em);
    }

    @Test
    @Transactional
    public void createRide() throws Exception {
        int databaseSizeBeforeCreate = rideRepository.findAll().size();

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);
        restRideMockMvc.perform(post("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isCreated());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeCreate + 1);
        Ride testRide = rideList.get(rideList.size() - 1);
        assertThat(testRide.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRide.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createRideWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rideRepository.findAll().size();

        // Create the Ride with an existing ID
        ride.setId(1L);
        RideDTO rideDTO = rideMapper.toDto(ride);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRideMockMvc.perform(post("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = rideRepository.findAll().size();
        // set the field null
        ride.setDate(null);

        // Create the Ride, which fails.
        RideDTO rideDTO = rideMapper.toDto(ride);

        restRideMockMvc.perform(post("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isBadRequest());

        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = rideRepository.findAll().size();
        // set the field null
        ride.setStatus(null);

        // Create the Ride, which fails.
        RideDTO rideDTO = rideMapper.toDto(ride);

        restRideMockMvc.perform(post("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isBadRequest());

        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRides() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList
        restRideMockMvc.perform(get("/api/rides?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ride.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get the ride
        restRideMockMvc.perform(get("/api/rides/{id}", ride.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ride.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRide() throws Exception {
        // Get the ride
        restRideMockMvc.perform(get("/api/rides/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        int databaseSizeBeforeUpdate = rideRepository.findAll().size();

        // Update the ride
        Ride updatedRide = rideRepository.findById(ride.getId()).get();
        // Disconnect from session so that the updates on updatedRide are not directly saved in db
        em.detach(updatedRide);
        updatedRide
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);
        RideDTO rideDTO = rideMapper.toDto(updatedRide);

        restRideMockMvc.perform(put("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isOk());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);
        Ride testRide = rideList.get(rideList.size() - 1);
        assertThat(testRide.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRide.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingRide() throws Exception {
        int databaseSizeBeforeUpdate = rideRepository.findAll().size();

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRideMockMvc.perform(put("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        int databaseSizeBeforeDelete = rideRepository.findAll().size();

        // Delete the ride
        restRideMockMvc.perform(delete("/api/rides/{id}", ride.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ride.class);
        Ride ride1 = new Ride();
        ride1.setId(1L);
        Ride ride2 = new Ride();
        ride2.setId(ride1.getId());
        assertThat(ride1).isEqualTo(ride2);
        ride2.setId(2L);
        assertThat(ride1).isNotEqualTo(ride2);
        ride1.setId(null);
        assertThat(ride1).isNotEqualTo(ride2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RideDTO.class);
        RideDTO rideDTO1 = new RideDTO();
        rideDTO1.setId(1L);
        RideDTO rideDTO2 = new RideDTO();
        assertThat(rideDTO1).isNotEqualTo(rideDTO2);
        rideDTO2.setId(rideDTO1.getId());
        assertThat(rideDTO1).isEqualTo(rideDTO2);
        rideDTO2.setId(2L);
        assertThat(rideDTO1).isNotEqualTo(rideDTO2);
        rideDTO1.setId(null);
        assertThat(rideDTO1).isNotEqualTo(rideDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rideMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rideMapper.fromId(null)).isNull();
    }
}
