package com.carpooling.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.carpooling.domain.Location} entity.
 */
public class LocationDTO implements Serializable {

    private Long id;

    @NotNull
    private String city;

    @NotNull
    private String area;

    @NotNull
    private Integer xAxis;

    @NotNull
    private Integer yAxis;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getxAxis() {
        return xAxis;
    }

    public void setxAxis(Integer xAxis) {
        this.xAxis = xAxis;
    }

    public Integer getyAxis() {
        return yAxis;
    }

    public void setyAxis(Integer yAxis) {
        this.yAxis = yAxis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocationDTO locationDTO = (LocationDTO) o;
        if (locationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), locationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + getId() +
            ", city='" + getCity() + "'" +
            ", area='" + getArea() + "'" +
            ", xAxis=" + getxAxis() +
            ", yAxis=" + getyAxis() +
            "}";
    }
}
