package com.tow.mandu.repository;

import com.tow.mandu.enums.ApproveStatus;
import com.tow.mandu.model.Provider;
import com.tow.mandu.model.User;
import com.tow.mandu.projection.BusinessProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    List<Provider> findByApproveStatusNot(ApproveStatus approveStatus);

    @Query("SELECT new com.tow.mandu.projection.BusinessProjection(p.id, u.fullName, p.locationLongitude, p.locationLatitude, p.user.phone) " +
            "FROM Provider p JOIN p.user u " +
            "WHERE p.geoHash IN :geohashes AND p.isActive = true")
    List<BusinessProjection> findTop30ByGeoHashes(@Param("geohashes") List<String> geohashes);


    Optional<Provider> findByUser(User user);

}
