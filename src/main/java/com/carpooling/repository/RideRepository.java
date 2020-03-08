package com.carpooling.repository;
import java.util.List;

import com.carpooling.domain.Ride;
import com.carpooling.domain.enumeration.Status;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ride entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findAllByStatus(Status status);
    List<Ride> findAllByDriver_Id(Long id);

}
