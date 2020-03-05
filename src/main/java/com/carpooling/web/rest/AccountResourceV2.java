package com.carpooling.web.rest;

import com.carpooling.domain.User;
import com.carpooling.service.CarPoolingUserService;
import com.carpooling.service.MailService;
import com.carpooling.service.UserService;
import com.carpooling.service.dto.CarPoolingUserDTO;
import com.carpooling.web.rest.errors.*;
import com.carpooling.web.rest.vm.ManagedUserVM;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.github.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/v2")
public class AccountResourceV2 {

    private final UserService userService;

    private final MailService mailService;

    private final CarPoolingUserService carPoolingUserService;
    private static final String ENTITY_NAME = "carPoolingUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public AccountResourceV2(UserService userService, MailService mailService,
            CarPoolingUserService carPoolingUserService) {
        this.userService = userService;
        this.mailService = mailService;
        this.carPoolingUserService = carPoolingUserService;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws URISyntaxException
     * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password
     *                                   is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is
     *                                   already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is
     *                                   already used.
     */
    @PostMapping("/register/user")
    public ResponseEntity<CarPoolingUserDTO> registerAccount(@Valid @RequestBody CarPoolingUserDTO carPoolingUser)
            throws URISyntaxException {
        if (!checkPasswordLength(carPoolingUser.getPassword())) {
            throw new InvalidPasswordException();
        }
        ManagedUserVM managedUserVM = mapToManagedUserVM(carPoolingUser);

        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        CarPoolingUserDTO result = carPoolingUserService.save(carPoolingUser);

        mailService.sendActivationEmail(user);
        return ResponseEntity.ok().body(result);
    }

    private ManagedUserVM mapToManagedUserVM(CarPoolingUserDTO userData){
        ManagedUserVM managedUserVM = new ManagedUserVM();
        Set<String> auth = new HashSet<>();
        auth.add("USER");
        managedUserVM.setActivated(true);
        managedUserVM.setEmail(userData.getEmail());
        managedUserVM.setFirstName(userData.getFirstName());
        managedUserVM.setLastName(userData.getLastName());
        managedUserVM.setPassword(userData.getPassword());
        managedUserVM.setAuthorities(auth);
        managedUserVM.setCreatedBy("Admin");
        managedUserVM.setLangKey("en");
        managedUserVM.setLogin(userData.getEmail());
        return managedUserVM;
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
