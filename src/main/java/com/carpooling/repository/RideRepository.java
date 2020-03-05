package com.carpooling.repository;
import com.carpooling.domain.Ride;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ride entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

}
