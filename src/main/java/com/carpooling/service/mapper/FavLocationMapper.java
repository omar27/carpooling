package com.carpooling.service.mapper;

import com.carpooling.domain.*;
import com.carpooling.service.dto.FavLocationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FavLocation} and its DTO {@link FavLocationDTO}.
 */
@Mapper(componentModel = "spring", uses = {CarPoolingUserMapper.class, LocationMapper.class})
public interface FavLocationMapper extends EntityMapper<FavLocationDTO, FavLocation> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "destination.id", target = "destinationId")
    @Mapping(source = "destination.city", target = "destinationCity")
    FavLocationDTO toDto(FavLocation favLocation);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "destinationId", target = "destination")
    FavLocation toEntity(FavLocationDTO favLocationDTO);

    default FavLocation fromId(Long id) {
        if (id == null) {
            return null;
        }
        FavLocation favLocation = new FavLocation();
        favLocation.setId(id);
        return favLocation;
    }
}
