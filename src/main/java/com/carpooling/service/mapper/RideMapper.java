package com.carpooling.service.mapper;

import com.carpooling.domain.*;
import com.carpooling.service.dto.RideDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ride} and its DTO {@link RideDTO}.
 */
@Mapper(componentModel = "spring", uses = {CarPoolingUserMapper.class, LocationMapper.class})
public interface RideMapper extends EntityMapper<RideDTO, Ride> {

    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(source = "driver.firstName", target = "driverFirstName")
    @Mapping(source = "passenger.id", target = "passengerId")
    @Mapping(source = "passenger.firstName", target = "passengerFirstName")
    @Mapping(source = "source.id", target = "sourceId")
    @Mapping(source = "source.city", target = "sourceCity")
    @Mapping(source = "destination.id", target = "destinationId")
    @Mapping(source = "destination.city", target = "destinationCity")
    RideDTO toDto(Ride ride);

    @Mapping(source = "driverId", target = "driver")
    @Mapping(source = "passengerId", target = "passenger")
    @Mapping(source = "sourceId", target = "source")
    @Mapping(source = "destinationId", target = "destination")
    Ride toEntity(RideDTO rideDTO);

    default Ride fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ride ride = new Ride();
        ride.setId(id);
        return ride;
    }
}
