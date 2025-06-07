package com.tow.mandu.service.impl;

import com.tow.mandu.enums.PaymentStatus;
import com.tow.mandu.enums.ServiceStatus;
import com.tow.mandu.model.ServiceRequest;
import com.tow.mandu.model.User;
import com.tow.mandu.pojo.DistanceCalculationPojo;
import com.tow.mandu.pojo.ServiceRequestPojo;
import com.tow.mandu.projection.AllServiceRequestProjection;
import com.tow.mandu.repository.ServiceRequestRepository;
import com.tow.mandu.repository.ServiceTypeRepository;
import com.tow.mandu.service.SeekerService;
import com.tow.mandu.service.ServiceRequestService;
import com.tow.mandu.utils.JwtUtil;
import com.tow.mandu.utils.LocationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.tow.mandu.enums.ServiceStatus.PENDING;

@Service
@RequiredArgsConstructor
public class ServiceRequestServiceImpl implements ServiceRequestService {


    private final ServiceRequestRepository serviceRequestRepository;
    private final LocationUtil locationUtil;
    private final ServiceTypeRepository serviceTypeRepository;
    private final JwtUtil jwtUtil;
    private final SeekerService seekerService;

    @Override
    public List<AllServiceRequestProjection> getAllServiceRequests() {
        List<AllServiceRequestProjection> serviceRequestProjections = serviceRequestRepository.findAllServiceRequestProjections();
        for (AllServiceRequestProjection projection : serviceRequestProjections) {
            DistanceCalculationPojo seekerLocation = new DistanceCalculationPojo();
            DistanceCalculationPojo providerLocation = new DistanceCalculationPojo();
            seekerLocation.setLatitude(projection.getServiceRequestLatitude());
            seekerLocation.setLongitude(projection.getServiceRequestLongitude());
            providerLocation.setLatitude(projection.getProviderLatitude());
            providerLocation.setLongitude(projection.getProviderLongitude());
            projection.setDistance(locationUtil.calculateVincentyDistance(seekerLocation, providerLocation)/1000);
            projection.setServiceRequestLocationLink(locationUtil.createGoogleMapsRedirectLink(projection.getServiceRequestLatitude(), projection.getServiceRequestLongitude()));
            projection.setServiceRequestLocation(locationUtil.getPlaceName(projection.getProviderLatitude(), projection.getProviderLongitude()));
        }
        return serviceRequestProjections;
    }

    @Override
    @Transactional
    public void save(ServiceRequestPojo serviceRequestPojo) {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setServiceRequestLatitude(serviceRequestPojo.getLatitude());
        serviceRequest.setServiceRequestLongitude(serviceRequestPojo.getLongitude());
        serviceRequest.setRequestTime(LocalDateTime.now());
        serviceRequest.setPaymentStatus(PaymentStatus.PENDING.name());
        serviceRequest.setServiceType(serviceTypeRepository.findServiceTypeByType(serviceRequestPojo.getServiceType().name()));
        serviceRequest.setSeeker(seekerService.getSeekerByUser(jwtUtil.getUserFromToken()));
        serviceRequest.setServiceStatus(PENDING);
        serviceRequest.setServiceDescriptionByUser(serviceRequestPojo.getDescription());
        serviceRequestRepository.save(serviceRequest);
    }

    @Override
    public Boolean hasPendingRequest() {
        User user = jwtUtil.getUserFromToken();
        if (user != null) {
            return serviceRequestRepository.existsBySeekerAndServiceStatusNot(seekerService.getSeekerByUser(user), ServiceStatus.COMPLETED);
        }
        return null;
    }


}
