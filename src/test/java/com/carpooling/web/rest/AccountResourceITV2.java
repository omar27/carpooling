package com.carpooling.web.rest;

import com.carpooling.CarpoolingApp;
import com.carpooling.domain.enumeration.UserType;
import com.carpooling.service.CarPoolingUserService;
import com.carpooling.service.MailService;
import com.carpooling.service.UserService;
import com.carpooling.service.dto.CarPoolingUserDTO;
import com.carpooling.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AccountResource} REST controller.
 */
@SpringBootTest(classes = CarpoolingApp.class)
public class AccountResourceITV2 {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpMessageConverter<?>[] httpMessageConverters;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Mock
    private UserService mockUserService;

    @Mock
    private MailService mockMailService;

    @Autowired
    private CarPoolingUserService mockCarPoolingUserService;

    private MockMvc restMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(mockMailService).sendActivationEmail(any());
        AccountResourceV2 accountResource = new AccountResourceV2(userService, mockMailService,
                mockCarPoolingUserService);

        this.restMvc = MockMvcBuilders.standaloneSetup(accountResource).setMessageConverters(httpMessageConverters)
                .setControllerAdvice(exceptionTranslator).build();

    }

    @Test
    public void withoutPayloadEndpointWillThrowBadRequest() throws Exception {
        restMvc.perform(post("/api/v2/register/user").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void withPayloadEndpointWillSendOkResponse() throws Exception {
        CarPoolingUserDTO userDto  = new CarPoolingUserDTO();
        userDto.setEmail("email@test.com");
        userDto.setFirstName("test1");
        userDto.setLastName("test2");
        userDto.setPhoneNumber("12345678912");
        userDto.setType(UserType.DRIVER);
        userDto.setPassword("somePassword");
        restMvc.perform(post("/api/v2/register/user").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userDto)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDto.getLastName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.type").value(UserType.DRIVER.toString()))
                .andExpect(jsonPath("$.phoneNumber").value(userDto.getPhoneNumber()));
    }

}
