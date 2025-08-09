package com.tow.mandu.repository;

import com.tow.mandu.enums.ServiceStatus;
import com.tow.mandu.model.Rider;
import com.tow.mandu.model.Seeker;
import com.tow.mandu.model.ServiceRequest;
import com.tow.mandu.model.User;
import com.tow.mandu.projection.AllServiceRequestProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    @Query("SELECT new com.tow.mandu.projection.AllServiceRequestProjection(" +
            "sr.id, " +
            "p.user.fullName, " +
            "s.user.fullName, " +
            "r.user.fullName, " +
            "sr.requestTime, " +
            "sr.acceptTime, " +
            "sr.serviceCompletionTime, " +
            "sr.serviceRequestLongitude, " +
            "sr.serviceRequestLatitude, " +
            "p.locationLongitude, " +
            "p.locationLatitude, " +
            "sr.distance, " +
            "sr.basePrice, " +
            "sr.finalPrice, " +
            "st.type, " +
            "sr.serviceStatus) " +
            "FROM ServiceRequest sr " +
            "JOIN sr.seeker s " +
            "JOIN sr.rider r " +
            "JOIN r.provider p " +
            "JOIN sr.serviceType st")
    List<AllServiceRequestProjection> findAllServiceRequestProjections();

    Boolean existsBySeekerAndServiceStatusNotIn(Seeker seeker, List<ServiceStatus> serviceStatusList);

    ServiceRequest findByRiderAndServiceStatusNotIn(Rider rider, List<ServiceStatus> serviceStatus);

    @Query("SELECT sr FROM ServiceRequest sr WHERE sr.serviceStatus = com.tow.mandu.enums.ServiceStatus.PENDING AND sr.seeker.id = :seekerId")
    ServiceRequest findPendingRequestsBySeekerId(@Param("seekerId") Long seekerId);

    List<ServiceRequest> findByRiderAndServiceStatus(Rider rider, ServiceStatus serviceStatus);

    @Query("SELECT sr FROM ServiceRequest sr WHERE sr.seeker.id = :seekerId ORDER BY sr.requestTime DESC limit 1")
    ServiceRequest findFirstBySeekerIdOrderByRequestTimeDesc(Long seekerId);


}
