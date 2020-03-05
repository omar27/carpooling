package com.carpooling.service.mapper;

import com.carpooling.domain.*;
import com.carpooling.service.dto.CarPoolingUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CarPoolingUser} and its DTO {@link CarPoolingUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CarPoolingUserMapper extends EntityMapper<CarPoolingUserDTO, CarPoolingUser> {



    default CarPoolingUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        CarPoolingUser carPoolingUser = new CarPoolingUser();
        carPoolingUser.setId(id);
        return carPoolingUser;
    }
}
