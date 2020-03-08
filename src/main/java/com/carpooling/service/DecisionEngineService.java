package com.carpooling.service;

import java.util.List;

import com.carpooling.domain.FavLocation;
import com.carpooling.domain.Ride;

/**
 * Service Interface for managing {@link com.carpooling.domain.CarPoolingUser}.
 */
public interface DecisionEngineService {

    abstract List<Ride> getRidesWithFavLocations(String email);

    abstract List<Ride> getOwnRides(String email);

    abstract List<FavLocation> getFavLocations(String email);
    
}
