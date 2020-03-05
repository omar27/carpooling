package com.carpooling.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.carpooling.domain.enumeration.Status;

/**
 * A DTO for the {@link com.carpooling.domain.Ride} entity.
 */
public class RideDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private Status status;


    private Long driverId;

    private String driverFirstName;

    private Long passengerId;

    private String passengerFirstName;

    private Long sourceId;

    private String sourceCity;

    private Long destinationId;

    private String destinationCity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long carPoolingUserId) {
        this.driverId = carPoolingUserId;
    }

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String carPoolingUserFirstName) {
        this.driverFirstName = carPoolingUserFirstName;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long carPoolingUserId) {
        this.passengerId = carPoolingUserId;
    }

    public String getPassengerFirstName() {
        return passengerFirstName;
    }

    public void setPassengerFirstName(String carPoolingUserFirstName) {
        this.passengerFirstName = carPoolingUserFirstName;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long locationId) {
        this.sourceId = locationId;
    }

    public String getSourceCity() {
        return sourceCity;
    }

    public void setSourceCity(String locationCity) {
        this.sourceCity = locationCity;
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

        RideDTO rideDTO = (RideDTO) o;
        if (rideDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rideDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RideDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", driver=" + getDriverId() +
            ", driver='" + getDriverFirstName() + "'" +
            ", passenger=" + getPassengerId() +
            ", passenger='" + getPassengerFirstName() + "'" +
            ", source=" + getSourceId() +
            ", source='" + getSourceCity() + "'" +
            ", destination=" + getDestinationId() +
            ", destination='" + getDestinationCity() + "'" +
            "}";
    }
}
