package com.carpooling.service.impl;

import com.carpooling.service.DecisionEngineService;
import com.carpooling.domain.CarPoolingUser;
import com.carpooling.domain.FavLocation;
import com.carpooling.domain.Location;
import com.carpooling.domain.Ride;
import com.carpooling.domain.enumeration.Status;
import com.carpooling.repository.CarPoolingUserRepository;
import com.carpooling.repository.FavLocationRepository;
import com.carpooling.repository.RideRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing {@link CarPoolingUser}.
 */
@Service
@Transactional
public class DecisionEngineServiceImpl implements DecisionEngineService {

    private final Logger log = LoggerFactory.getLogger(DecisionEngineServiceImpl.class);
    private final CarPoolingUserRepository carPoolingUserRepository;
    private final FavLocationRepository favLocationRepository;
    private final RideRepository rideRepository;

    public DecisionEngineServiceImpl(RideRepository rideRepository, CarPoolingUserRepository carPoolingUserRepository,
            FavLocationRepository favLocationRepository) {
        this.rideRepository = rideRepository;
        this.carPoolingUserRepository = carPoolingUserRepository;
        this.favLocationRepository = favLocationRepository;
    }

    @Override
    public List<Ride> getRidesWithFavLocations(String email) {
        List<Ride> favLocRides = new ArrayList<Ride>();
        if (email != null) {
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
        List<Ride> rides = null;
        rides = rideRepository.findAllByStatus(Status.SCHEDULED); // Only get rides which are scheduled
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
        if ((Math.abs(fav.getxAxis() - ride.getxAxis()) <= 20) && (Math.abs(fav.getyAxis() - ride.getyAxis()) <= 20)) {
            return true;
        }
        return false;
    }

    @Override
    public List<Ride> getOwnRides(String email) {
        List<Ride> ownRides = new ArrayList<Ride>();
        if (email != null) {
            CarPoolingUser user = getUserDetails(email);
            if (user != null) {
                ownRides = getRidesByDriver(user);
                return ownRides;
            }
        }
        return ownRides;
    }

    private List<Ride> getRidesByDriver(CarPoolingUser user) {
        List<Ride> rides = rideRepository.findAllByDriver_Id(user.getId());
        return rides;
    }

    @Override
    public List<FavLocation> getFavLocations(String email) {
        List<FavLocation> favLocations = new ArrayList<FavLocation>();
        if (email != null) {
            CarPoolingUser user = getUserDetails(email);
            if (user != null) {
                favLocations = getFavLocationsList(user);
                return favLocations;
            }
        }
        return favLocations;
    }

}
