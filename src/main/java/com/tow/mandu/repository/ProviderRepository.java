package com.tow.mandu.repository;

import com.tow.mandu.enums.ApproveStatus;
import com.tow.mandu.model.Provider;
import com.tow.mandu.model.User;
import com.tow.mandu.projection.BusinessProjection;
import com.tow.mandu.projection.ProviderDashboardDataProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    List<Provider> findByApproveStatusNot(ApproveStatus approveStatus);

    @Query("""
            SELECT new com.tow.mandu.projection.BusinessProjection(
                p.id, u.fullName, p.locationLongitude, p.locationLatitude, p.user.phone
            )
            FROM Provider p
            JOIN p.user u
            WHERE p.geoHash IN :geohashes
              AND p.isActive = true
              AND EXISTS (
                SELECT r.id FROM Rider r
                WHERE r.provider = p
                  AND r.status = com.tow.mandu.enums.RiderStatus.ACTIVE
              )
            """)
    List<BusinessProjection> findTop30ByGeoHashes(@Param("geohashes") List<String> geohashes);


    @Query(nativeQuery = true, value =
            "SELECT " +
                    "    p.id AS id, " +
                    "    u.full_name AS full_name, " +
                    "    COALESCE((SELECT COUNT(*) FROM rider r WHERE r.provider_id = p.id AND r.status = 'ONLINE'), 0) AS online_riders, " +
                    "    COALESCE((SELECT COUNT(*) FROM rider r WHERE r.provider_id = p.id), 0) AS total_riders, " +
                    "    COALESCE((SELECT COUNT(*) FROM service_request sr JOIN rider r ON sr.rider_id = r.id WHERE r.provider_id = p.id AND sr.service_status = 'PENDING'), 0) AS pending_requests, " +
                    "    COALESCE((SELECT COUNT(*) FROM service_request sr JOIN rider r ON sr.rider_id = r.id WHERE r.provider_id = p.id AND sr.service_status = 'IN_PROGRESS'), 0) AS work_on_progress, " +
                    "    COALESCE((SELECT COUNT(*) FROM service_request sr JOIN rider r ON sr.rider_id = r.id WHERE r.provider_id = p.id AND sr.service_status = 'COMPLETED' AND DATE(sr.service_completion_time) = CURRENT_DATE), 0) AS completed_today " +
                    "FROM provider p " +
                    "JOIN user u ON p.user_id = u.id " +
                    "WHERE p.id = :id")
    ProviderDashboardDataProjection findProviderDashboardDataById(@Param("id") Long id);

    Optional<Provider> findByUser(User user);

}
