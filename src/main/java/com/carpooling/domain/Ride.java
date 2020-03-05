package com.carpooling.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

import com.carpooling.domain.enumeration.Status;

/**
 * A Ride.
 */
@Entity
@Table(name = "ride")
public class Ride implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("rides")
    private CarPoolingUser driver;

    @ManyToOne
    @JsonIgnoreProperties("rides")
    private CarPoolingUser passenger;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("rides")
    private Location source;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("rides")
    private Location destination;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Ride date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public Ride status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CarPoolingUser getDriver() {
        return driver;
    }

    public Ride driver(CarPoolingUser carPoolingUser) {
        this.driver = carPoolingUser;
        return this;
    }

    public void setDriver(CarPoolingUser carPoolingUser) {
        this.driver = carPoolingUser;
    }

    public CarPoolingUser getPassenger() {
        return passenger;
    }

    public Ride passenger(CarPoolingUser carPoolingUser) {
        this.passenger = carPoolingUser;
        return this;
    }

    public void setPassenger(CarPoolingUser carPoolingUser) {
        this.passenger = carPoolingUser;
    }

    public Location getSource() {
        return source;
    }

    public Ride source(Location location) {
        this.source = location;
        return this;
    }

    public void setSource(Location location) {
        this.source = location;
    }

    public Location getDestination() {
        return destination;
    }

    public Ride destination(Location location) {
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
        if (!(o instanceof Ride)) {
            return false;
        }
        return id != null && id.equals(((Ride) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Ride{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
