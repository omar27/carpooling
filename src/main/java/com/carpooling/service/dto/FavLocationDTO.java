package com.carpooling.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.carpooling.domain.FavLocation} entity.
 */
public class FavLocationDTO implements Serializable {

    private Long id;


    private Long userId;

    private String userFirstName;

    private Long destinationId;

    private String destinationCity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long carPoolingUserId) {
        this.userId = carPoolingUserId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String carPoolingUserFirstName) {
        this.userFirstName = carPoolingUserFirstName;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long locationId) {
        this.destinationId = locationId;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String locationCity) {
        this.destinationCity = locationCity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FavLocationDTO favLocationDTO = (FavLocationDTO) o;
        if (favLocationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), favLocationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FavLocationDTO{" +
            "id=" + getId() +
            ", user=" + getUserId() +
            ", user='" + getUserFirstName() + "'" +
            ", destination=" + getDestinationId() +
            ", destination='" + getDestinationCity() + "'" +
            "}";
    }
}
