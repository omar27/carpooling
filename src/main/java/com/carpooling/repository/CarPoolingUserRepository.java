package com.carpooling.repository;
import java.util.Optional;

import com.carpooling.domain.CarPoolingUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CarPoolingUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarPoolingUserRepository extends JpaRepository<CarPoolingUser, Long> {
    Optional<CarPoolingUser> findOneByEmail(String email);

}
