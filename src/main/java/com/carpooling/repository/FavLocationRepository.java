package com.carpooling.repository;
import java.util.List;

import com.carpooling.domain.FavLocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FavLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavLocationRepository extends JpaRepository<FavLocation, Long> {
    List<FavLocation> findAllByUser_Id(Long id);
}
