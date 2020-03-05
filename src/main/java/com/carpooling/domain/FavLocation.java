package com.carpooling.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A FavLocation.
 */
@Entity
@Table(name = "fav_location")
public class FavLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("favLocations")
    private CarPoolingUser user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("favLocations")
    private Location destination;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CarPoolingUser getUser() {
        return user;
    }

    public FavLocation user(CarPoolingUser carPoolingUser) {
        this.user = carPoolingUser;
        return this;
    }

    public void setUser(CarPoolingUser carPoolingUser) {
        this.user = carPoolingUser;
    }

    public Location getDestination() {
        return destination;
    }

    public FavLocation destination(Location location) {
        this.destination = location;
        return this;
    }

    public void setDestination(Location location) {
        this.destination = location;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FavLocation)) {
            return false;
        }
        return id != null && id.equals(((FavLocation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FavLocation{" +
            "id=" + getId() +
            "}";
    }
}
