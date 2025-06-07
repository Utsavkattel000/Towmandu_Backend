package com.tow.mandu.repository;

import com.tow.mandu.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.serviceRequest.rider.provider.id = :businessId")
    List<Review> findByProviderId(@Param("businessId") Long businessId);


}
