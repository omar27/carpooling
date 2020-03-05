package com.carpooling.service.impl;

import com.carpooling.service.DecisionEngineService;
import com.carpooling.service.UserService;
import com.carpooling.service.dto.UserDTO;
import com.carpooling.web.rest.AccountResource;
import com.carpooling.web.websocket.dto.ActivityDTO;
import com.carpooling.domain.CarPoolingUser;
import com.carpooling.domain.FavLocation;
import com.carpooling.domain.Location;
import com.carpooling.domain.Ride;
import com.carpooling.domain.User;
import com.carpooling.repository.CarPoolingUserRepository;
import com.carpooling.repository.FavLocationRepository;
import com.carpooling.repository.RideRepository;
import com.carpooling.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing {@link CarPoolingUser}.
 */
@Service
@Transactional
public class DecisionEngineServiceImpl implements DecisionEngineService {

    private final Logger log = LoggerFactory.getLogger(DecisionEngineServiceImpl.class);
    private final SimpMessageSendingOperations messagingTemplate;
    private final CarPoolingUserRepository carPoolingUserRepository;
    private final FavLocationRepository favLocationRepository;
    private final RideRepository rideRepository;
    private final UserService userService;
    private String email;

    public DecisionEngineServiceImpl(SimpMessageSendingOperations messagingTemplate, RideRepository rideRepository,
            CarPoolingUserRepository carPoolingUserRepository, FavLocationRepository favLocationRepository,
            UserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.rideRepository = rideRepository;
        this.carPoolingUserRepository = carPoolingUserRepository;
        this.favLocationRepository = favLocationRepository;
        this.userService = userService;
    }

    @PostMapping("/data")
    public void message(@RequestBody String email) {
        this.email = email;
    }

    @Scheduled(fixedRate = 3000)
    public void sendRidesToUI() {
        // log.debug("is user authenticated _____ "+SecurityUtils.isAuthenticated());
        // log.debug("is USER isCurrentUserInRole _____
        // "+SecurityUtils.isCurrentUserInRole("ROLE_USER"));
        // if (SecurityUtils.isAuthenticated() &&
        // SecurityUtils.isCurrentUserInRole("ROLE_USER")) { // Only send to UI if
        // logged
        // // in role is USER
        this.messagingTemplate.convertAndSend("/topic/notifications", getRidesWithFavLocations());
        // }
    }

    public List<Ride> getRidesWithFavLocations() {
        List<Ride> favLocRides = new ArrayList<Ride>();
        if (this.email != null) {
            CarPoolingUser user = getUserDetails(email);
            if (user != null) {
                List<Ride> allRides = getAllRides();
                List<FavLocation> favLocations = getFavLocationsList(user);
                favLocRides = findRidesForFavLocations(allRides, favLocations);
                return favLocRides;
            }
        }
        return favLocRides;
    }

    public List<Ride> getAllRides() {
        List<Ride> rides = rideRepository.findAll();
        return rides;
    }

    public CarPoolingUser getUserDetails(String email) {
        CarPoolingUser user = carPoolingUserRepository.findOneByEmail(email).isPresent()
                ? carPoolingUserRepository.findOneByEmail(email).get()
                : null;
        return user;
    }

    public List<FavLocation> getFavLocationsList(CarPoolingUser user) {
        List<FavLocation> locations = favLocationRepository.findAllByUser_Id(user.getId());
        return locations;
    }

    public List<Ride> findRidesForFavLocations(List<Ride> allRides, List<FavLocation> favLocations) {
        List<Ride> rides = new ArrayList<Ride>();
        allRides.stream().forEach(ride -> {
            favLocations.stream().forEach(favLoc -> {
                if (favLoc.getDestination().getCity().equals(ride.getDestination().getCity())) {
                    if (isDestinationNearFavLocation(favLoc.getDestination(), ride.getDestination())) {
                        rides.add(ride);
                    }
                }
            });
        });
        return rides;
    }

    public boolean isDestinationNearFavLocation(Location fav, Location ride) {
        if ((fav.getxAxis() <= ride.getxAxis() || fav.getxAxis() >= ride.getxAxis())
                && (fav.getyAxis() <= ride.getyAxis() || fav.getyAxis() >= ride.getyAxis())) {
            return true;
        }
        return false;
    }

}
