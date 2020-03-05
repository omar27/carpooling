package com.carpooling.web.rest;

import com.carpooling.CarpoolingApp;
import com.carpooling.domain.CarPoolingUser;
import com.carpooling.repository.CarPoolingUserRepository;
import com.carpooling.service.CarPoolingUserService;
import com.carpooling.service.dto.CarPoolingUserDTO;
import com.carpooling.service.mapper.CarPoolingUserMapper;
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

import com.carpooling.domain.enumeration.UserType;
/**
 * Integration tests for the {@link CarPoolingUserResource} REST controller.
 */
@SpringBootTest(classes = CarpoolingApp.class)
public class CarPoolingUserResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final UserType DEFAULT_TYPE = UserType.DRIVER;
    private static final UserType UPDATED_TYPE = UserType.PASSENGER;

    @Autowired
    private CarPoolingUserRepository carPoolingUserRepository;

    @Autowired
    private CarPoolingUserMapper carPoolingUserMapper;

    @Autowired
    private CarPoolingUserService carPoolingUserService;

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

    private MockMvc restCarPoolingUserMockMvc;

    private CarPoolingUser carPoolingUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CarPoolingUserResource carPoolingUserResource = new CarPoolingUserResource(carPoolingUserService);
        this.restCarPoolingUserMockMvc = MockMvcBuilders.standaloneSetup(carPoolingUserResource)
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
    public static CarPoolingUser createEntity(EntityManager em) {
        CarPoolingUser carPoolingUser = new CarPoolingUser()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .password(DEFAULT_PASSWORD)
            .type(DEFAULT_TYPE);
        return carPoolingUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarPoolingUser createUpdatedEntity(EntityManager em) {
        CarPoolingUser carPoolingUser = new CarPoolingUser()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .password(UPDATED_PASSWORD)
            .type(UPDATED_TYPE);
        return carPoolingUser;
    }

    @BeforeEach
    public void initTest() {
        carPoolingUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarPoolingUser() throws Exception {
        int databaseSizeBeforeCreate = carPoolingUserRepository.findAll().size();

        // Create the CarPoolingUser
        CarPoolingUserDTO carPoolingUserDTO = carPoolingUserMapper.toDto(carPoolingUser);
        restCarPoolingUserMockMvc.perform(post("/api/car-pooling-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carPoolingUserDTO)))
            .andExpect(status().isCreated());

        // Validate the CarPoolingUser in the database
        List<CarPoolingUser> carPoolingUserList = carPoolingUserRepository.findAll();
        assertThat(carPoolingUserList).hasSize(databaseSizeBeforeCreate + 1);
        CarPoolingUser testCarPoolingUser = carPoolingUserList.get(carPoolingUserList.size() - 1);
        assertThat(testCarPoolingUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCarPoolingUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCarPoolingUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCarPoolingUser.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCarPoolingUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testCarPoolingUser.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createCarPoolingUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carPoolingUserRepository.findAll().size();

        // Create the CarPoolingUser with an existing ID
        carPoolingUser.setId(1L);
        CarPoolingUserDTO carPoolingUserDTO = carPoolingUserMapper.toDto(carPoolingUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarPoolingUserMockMvc.perform(post("/api/car-pooling-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carPoolingUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CarPoolingUser in the database
        List<CarPoolingUser> carPoolingUserList = carPoolingUserRepository.findAll();
        assertThat(carPoolingUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = carPoolingUserRepository.findAll().size();
        // set the field null
        carPoolingUser.setFirstName(null);

        // Create the CarPoolingUser, which fails.
        CarPoolingUserDTO carPoolingUserDTO = carPoolingUserMapper.toDto(carPoolingUser);

        restCarPoolingUserMockMvc.perform(post("/api/car-pooling-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carPoolingUserDTO)))
            .andExpect(status().isBadRequest());

        List<CarPoolingUser> carPoolingUserList = carPoolingUserRepository.findAll();
        assertThat(carPoolingUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = carPoolingUserRepository.findAll().size();
        // set the field null
        carPoolingUser.setLastName(null);

        // Create the CarPoolingUser, which fails.
        CarPoolingUserDTO carPoolingUserDTO = carPoolingUserMapper.toDto(carPoolingUser);

        restCarPoolingUserMockMvc.perform(post("/api/car-pooling-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carPoolingUserDTO)))
            .andExpect(status().isBadRequest());

        List<CarPoolingUser> carPoolingUserList = carPoolingUserRepository.findAll();
        assertThat(carPoolingUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = carPoolingUserRepository.findAll().size();
        // set the field null
        carPoolingUser.setEmail(null);

        // Create the CarPoolingUser, which fails.
        CarPoolingUserDTO carPoolingUserDTO = carPoolingUserMapper.toDto(carPoolingUser);

        restCarPoolingUserMockMvc.perform(post("/api/car-pooling-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carPoolingUserDTO)))
            .andExpect(status().isBadRequest());

        List<CarPoolingUser> carPoolingUserList = carPoolingUserRepository.findAll();
        assertThat(carPoolingUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = carPoolingUserRepository.findAll().size();
        // set the field null
        carPoolingUser.setPhoneNumber(null);

        // Create the CarPoolingUser, which fails.
        CarPoolingUserDTO carPoolingUserDTO = carPoolingUserMapper.toDto(carPoolingUser);

        restCarPoolingUserMockMvc.perform(post("/api/car-pooling-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carPoolingUserDTO)))
            .andExpect(status().isBadRequest());

        List<CarPoolingUser> carPoolingUserList = carPoolingUserRepository.findAll();
        assertThat(carPoolingUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = carPoolingUserRepository.findAll().size();
        // set the field null
        carPoolingUser.setPassword(null);

        // Create the CarPoolingUser, which fails.
        CarPoolingUserDTO carPoolingUserDTO = carPoolingUserMapper.toDto(carPoolingUser);

        restCarPoolingUserMockMvc.perform(post("/api/car-pooling-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carPoolingUserDTO)))
            .andExpect(status().isBadRequest());

        List<CarPoolingUser> carPoolingUserList = carPoolingUserRepository.findAll();
        assertThat(carPoolingUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCarPoolingUsers() throws Exception {
        // Initialize the database
        carPoolingUserRepository.saveAndFlush(carPoolingUser);

        // Get all the carPoolingUserList
        restCarPoolingUserMockMvc.perform(get("/api/car-pooling-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carPoolingUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getCarPoolingUser() throws Exception {
        // Initialize the database
        carPoolingUserRepository.saveAndFlush(carPoolingUser);

        // Get the carPoolingUser
        restCarPoolingUserMockMvc.perform(get("/api/car-pooling-users/{id}", carPoolingUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(carPoolingUser.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCarPoolingUser() throws Exception {
        // Get the carPoolingUser
        restCarPoolingUserMockMvc.perform(get("/api/car-pooling-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarPoolingUser() throws Exception {
        // Initialize the database
        carPoolingUserRepository.saveAndFlush(carPoolingUser);

        int databaseSizeBeforeUpdate = carPoolingUserRepository.findAll().size();

        // Update the carPoolingUser
        CarPoolingUser updatedCarPoolingUser = carPoolingUserRepository.findById(carPoolingUser.getId()).get();
        // Disconnect from session so that the updates on updatedCarPoolingUser are not directly saved in db
        em.detach(updatedCarPoolingUser);
        updatedCarPoolingUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .password(UPDATED_PASSWORD)
            .type(UPDATED_TYPE);
        CarPoolingUserDTO carPoolingUserDTO = carPoolingUserMapper.toDto(updatedCarPoolingUser);

        restCarPoolingUserMockMvc.perform(put("/api/car-pooling-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carPoolingUserDTO)))
            .andExpect(status().isOk());

        // Validate the CarPoolingUser in the database
        List<CarPoolingUser> carPoolingUserList = carPoolingUserRepository.findAll();
        assertThat(carPoolingUserList).hasSize(databaseSizeBeforeUpdate);
        CarPoolingUser testCarPoolingUser = carPoolingUserList.get(carPoolingUserList.size() - 1);
        assertThat(testCarPoolingUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCarPoolingUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCarPoolingUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCarPoolingUser.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCarPoolingUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testCarPoolingUser.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingCarPoolingUser() throws Exception {
        int databaseSizeBeforeUpdate = carPoolingUserRepository.findAll().size();

        // Create the CarPoolingUser
        CarPoolingUserDTO carPoolingUserDTO = carPoolingUserMapper.toDto(carPoolingUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarPoolingUserMockMvc.perform(put("/api/car-pooling-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carPoolingUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CarPoolingUser in the database
        List<CarPoolingUser> carPoolingUserList = carPoolingUserRepository.findAll();
        assertThat(carPoolingUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCarPoolingUser() throws Exception {
        // Initialize the database
        carPoolingUserRepository.saveAndFlush(carPoolingUser);

        int databaseSizeBeforeDelete = carPoolingUserRepository.findAll().size();

        // Delete the carPoolingUser
        restCarPoolingUserMockMvc.perform(delete("/api/car-pooling-users/{id}", carPoolingUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CarPoolingUser> carPoolingUserList = carPoolingUserRepository.findAll();
        assertThat(carPoolingUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarPoolingUser.class);
        CarPoolingUser carPoolingUser1 = new CarPoolingUser();
        carPoolingUser1.setId(1L);
        CarPoolingUser carPoolingUser2 = new CarPoolingUser();
        carPoolingUser2.setId(carPoolingUser1.getId());
        assertThat(carPoolingUser1).isEqualTo(carPoolingUser2);
        carPoolingUser2.setId(2L);
        assertThat(carPoolingUser1).isNotEqualTo(carPoolingUser2);
        carPoolingUser1.setId(null);
        assertThat(carPoolingUser1).isNotEqualTo(carPoolingUser2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarPoolingUserDTO.class);
        CarPoolingUserDTO carPoolingUserDTO1 = new CarPoolingUserDTO();
        carPoolingUserDTO1.setId(1L);
        CarPoolingUserDTO carPoolingUserDTO2 = new CarPoolingUserDTO();
        assertThat(carPoolingUserDTO1).isNotEqualTo(carPoolingUserDTO2);
        carPoolingUserDTO2.setId(carPoolingUserDTO1.getId());
        assertThat(carPoolingUserDTO1).isEqualTo(carPoolingUserDTO2);
        carPoolingUserDTO2.setId(2L);
        assertThat(carPoolingUserDTO1).isNotEqualTo(carPoolingUserDTO2);
        carPoolingUserDTO1.setId(null);
        assertThat(carPoolingUserDTO1).isNotEqualTo(carPoolingUserDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(carPoolingUserMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(carPoolingUserMapper.fromId(null)).isNull();
    }
}
