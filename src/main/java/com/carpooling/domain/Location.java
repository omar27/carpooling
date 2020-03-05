package com.carpooling.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "area", nullable = false)
    private String area;

    @NotNull
    @Column(name = "x_axis", nullable = false)
    private Integer xAxis;

    @NotNull
    @Column(name = "y_axis", nullable = false)
    private Integer yAxis;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public Location city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public Location area(String area) {
        this.area = area;
        return this;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getxAxis() {
        return xAxis;
    }

    public Location xAxis(Integer xAxis) {
        this.xAxis = xAxis;
        return this;
    }

    public void setxAxis(Integer xAxis) {
        this.xAxis = xAxis;
    }

    public Integer getyAxis() {
        return yAxis;
    }

    public Location yAxis(Integer yAxis) {
        this.yAxis = yAxis;
        return this;
    }

    public void setyAxis(Integer yAxis) {
        this.yAxis = yAxis;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", city='" + getCity() + "'" +
            ", area='" + getArea() + "'" +
            ", xAxis=" + getxAxis() +
            ", yAxis=" + getyAxis() +
            "}";
    }
}
