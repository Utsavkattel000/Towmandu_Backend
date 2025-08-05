package com.tow.mandu.repository;

import com.tow.mandu.model.Rider;
import com.tow.mandu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RiderRepository extends JpaRepository<Rider, Long> {

    List<Rider> findByProviderId(Long id);

    @Query(value = "SELECT * FROM rider r WHERE r.provider_id = :providerId AND r.status = 'ACTIVE' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Rider> findRandomActiveRiderByProviderId(@Param("providerId") Long providerId);


    Rider findByUser(User user);
}
