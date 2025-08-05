package com.tow.mandu.service.impl;

import com.tow.mandu.config.firebase.Notification;
import com.tow.mandu.enums.*;
import com.tow.mandu.enums.ServiceType;
import com.tow.mandu.model.*;
import com.tow.mandu.pojo.DistanceCalculationPojo;
import com.tow.mandu.pojo.ServiceRequestPojo;
import com.tow.mandu.projection.AllServiceRequestProjection;
import com.tow.mandu.projection.BusinessProjection;
import com.tow.mandu.projection.ServiceRequestDetailProjection;
import com.tow.mandu.repository.RiderAssignmentRepository;
import com.tow.mandu.repository.RiderRepository;
import com.tow.mandu.repository.ServiceRequestRepository;
import com.tow.mandu.repository.ServiceTypeRepository;
import com.tow.mandu.service.NotificationService;
import com.tow.mandu.service.ProviderService;
import com.tow.mandu.service.SeekerService;
import com.tow.mandu.service.ServiceRequestService;
import com.tow.mandu.utils.JwtUtil;
import com.tow.mandu.utils.LocationUtil;
import com.tow.mandu.utils.NotificationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final ProviderService providerService;
    private final RiderRepository riderRepository;
    private final RiderAssignmentRepository riderAssignmentRepository;
    private final NotificationService notificationService;

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
            projection.setDistance(locationUtil.calculateVincentyDistance(seekerLocation, providerLocation) / 1000);
            projection.setServiceRequestLocationLink(locationUtil.createGoogleMapsRedirectLink(projection.getServiceRequestLatitude(), projection.getServiceRequestLongitude()));
            projection.setServiceRequestLocation(locationUtil.getPlaceName(projection.getProviderLatitude(), projection.getProviderLongitude()));
        }
        return serviceRequestProjections;
    }

    @Override
    @Transactional
    public List<BusinessProjection> save(ServiceRequestPojo serviceRequestPojo) {
        ServiceRequest serviceRequest = new ServiceRequest();
        System.out.println("longitude: " + serviceRequestPojo.getLongitude());
        System.out.println("latitude: " + serviceRequestPojo.getLatitude());
        System.out.println("Geohash: " + locationUtil.getGeoHash(serviceRequestPojo.getLatitude(), serviceRequestPojo.getLongitude()));
        serviceRequest.setServiceRequestLatitude(serviceRequestPojo.getLatitude());
        serviceRequest.setServiceRequestLongitude(serviceRequestPojo.getLongitude());
        serviceRequest.setRequestTime(LocalDateTime.now());
        serviceRequest.setPaymentStatus(PaymentStatus.PENDING.name());
        serviceRequest.setServiceType(serviceTypeRepository.findServiceTypeByType(serviceRequestPojo.getServiceType().name()));
        serviceRequest.setVehicleType(serviceRequestPojo.getVehicleType());
        serviceRequest.setSeeker(seekerService.getSeekerByUser(jwtUtil.getUserFromToken()));
        serviceRequest.setServiceStatus(PENDING);
        serviceRequest.setServiceDescriptionByUser(serviceRequestPojo.getDescription());
        serviceRequestRepository.save(serviceRequest);
        if (!providerService.getNearestProvidersByLocation(serviceRequestPojo.getLatitude(),
                serviceRequestPojo.getLongitude()).isEmpty()) {
            return providerService.getNearestProvidersByLocation(serviceRequestPojo.getLatitude(),
                    serviceRequestPojo.getLongitude());
        } else {
            serviceRequest.setServiceStatus(ServiceStatus.UNAVAILABLE);
            serviceRequestRepository.save(serviceRequest);
            return null;
        }
    }

    @Override
    public Boolean hasPendingRequest() {
        User user = jwtUtil.getUserFromToken();
        if (user != null) {
            List<ServiceStatus> excludedStatuses = List.of(ServiceStatus.COMPLETED, ServiceStatus.CANCELLED,
                    ServiceStatus.UNAVAILABLE, ServiceStatus.REJECTED);
            return serviceRequestRepository.existsBySeekerAndServiceStatusNotIn(
                    seekerService.getSeekerByUser(user),
                    excludedStatuses
            );
        }
        return null;
    }

    @Override
    @Transactional
    public Boolean updateStatus(Long serviceRequestId, ServiceStatus status) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(serviceRequestId).orElse(null);
        if (serviceRequest != null) {
            serviceRequest.setServiceStatus(status);
            serviceRequestRepository.save(serviceRequest);
            if(status.equals(ServiceStatus.ACCEPTED) || status.equals(ServiceStatus.IN_PROGRESS)) {
                Rider rider = serviceRequest.getRider();
                rider.setStatus(RiderStatus.BUSY);
            }
            Notification notification = NotificationUtil.buildNotification(serviceRequest.getSeeker().getUser(),
                    "Service Request Status Update",
                    "Your service request has been " + status.name() + " by "
                            + serviceRequest.getRider().getProvider().getUser().getFullName() + ". Rider "
                            + serviceRequest.getRider().getUser().getFullName());
            notificationService.saveAndSend(notification);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean selectProvider(Long serviceRequestId, Long providerId) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(serviceRequestId).orElseThrow();
        riderRepository.findRandomActiveRiderByProviderId(providerId);
        serviceRequest.setRider(riderRepository.findRandomActiveRiderByProviderId(providerId).orElseThrow());
        RiderAssignment riderAssignment = new RiderAssignment();
        riderAssignment.setRider(serviceRequest.getRider());
        riderAssignment.setServiceRequest(serviceRequest);
        riderAssignment.setAssignedTime(LocalDateTime.now());
        riderAssignment.setStatus(PENDING.name());
        riderAssignmentRepository.save(riderAssignment);
        serviceRequestRepository.save(serviceRequest);
        Notification notification = NotificationUtil.buildNotification(serviceRequest.getRider().getUser(),
                "New Service Request",
                "You have a new service request from " + serviceRequest.getSeeker().getUser().getFullName() +
                        " for " + serviceRequest.getServiceType().getType() + " for a " + serviceRequest.getVehicleType()
        );
        notificationService.saveAndSend(notification);
        return true;
    }

    @Override
    public Long getCurrentServiceRequestId() {
        User user = jwtUtil.getUserFromToken();
        Seeker seeker = seekerService.getSeekerByUser(user);
        Rider rider = riderRepository.findByUser(user);
        if (seeker != null) {
            ServiceRequest serviceRequest = serviceRequestRepository.findPendingRequestsBySeekerId(seeker.getId());
            if (serviceRequest != null) {
                return serviceRequest.getId();
            }
        } else if (rider != null) {
            ServiceRequest serviceRequest = serviceRequestRepository.findByRiderAndServiceStatusNotIn(
                    rider, List.of(ServiceStatus.COMPLETED, ServiceStatus.CANCELLED, ServiceStatus.UNAVAILABLE, ServiceStatus.REJECTED));
            return serviceRequest.getId();
        }
        return null;
    }

    @Override
    public List<ServiceRequestDetailProjection> getRiderServiceRequest(){
        Rider rider = riderRepository.findByUser(jwtUtil.getUserFromToken());
        List<ServiceRequest> serviceRequests =serviceRequestRepository.findByRiderAndServiceStatus(rider,PENDING);
        List<ServiceRequestDetailProjection> serviceRequestDetailProjections = new ArrayList<>();
        for (ServiceRequest serviceRequest : serviceRequests) {
            ServiceRequestDetailProjection serviceRequestDetailProjection = new ServiceRequestDetailProjection();
            serviceRequestDetailProjection.setId(serviceRequest.getId());
            serviceRequestDetailProjection.setPhone(serviceRequest.getSeeker().getUser().getPhone());
            serviceRequestDetailProjection.setLocationLongitude(serviceRequest.getServiceRequestLongitude());
            serviceRequestDetailProjection.setLocationLatitude(serviceRequest.getServiceRequestLatitude());
            DistanceCalculationPojo providerLocation = new DistanceCalculationPojo();
            DistanceCalculationPojo seekerLocation = new DistanceCalculationPojo();
            providerLocation.setLatitude(serviceRequest.getRider().getProvider().getLocationLatitude());
            providerLocation.setLongitude(serviceRequest.getRider().getProvider().getLocationLongitude());
            seekerLocation.setLatitude(serviceRequest.getServiceRequestLatitude());
            seekerLocation.setLongitude(serviceRequest.getServiceRequestLongitude());
            Double distance = locationUtil.calculateVincentyDistance(providerLocation, seekerLocation);
            int price = distance < 1000 ? 50 : (int) Math.ceil(distance / 1000) * 50;
            serviceRequestDetailProjection.setPrice("Rs. "+price);
            serviceRequestDetailProjection.setEstimatedDistance(distance);
            serviceRequestDetailProjection.setSeekerName(serviceRequest.getSeeker().getUser().getFullName());
            serviceRequestDetailProjection.setServiceType(ServiceType.valueOf(serviceRequest.getServiceType().getType()));
            serviceRequestDetailProjection.setVehicleType(serviceRequest.getVehicleType());
            serviceRequestDetailProjection.setMessage(serviceRequest.getServiceDescriptionByUser());
            serviceRequestDetailProjections.add(serviceRequestDetailProjection);

        }

        return serviceRequestDetailProjections;
    }

    @Override
    public ServiceRequestDetailProjection getRiderServiceRequestDetails(Long Id){
            ServiceRequest serviceRequest =serviceRequestRepository.findById(Id).orElseThrow();
            ServiceRequestDetailProjection serviceRequestDetailProjection = new ServiceRequestDetailProjection();
            serviceRequestDetailProjection.setId(serviceRequest.getId());
            serviceRequestDetailProjection.setPhone(serviceRequest.getSeeker().getUser().getPhone());
            serviceRequestDetailProjection.setLocationLongitude(serviceRequest.getServiceRequestLongitude());
            serviceRequestDetailProjection.setLocationLatitude(serviceRequest.getServiceRequestLatitude());
            DistanceCalculationPojo providerLocation = new DistanceCalculationPojo();
            DistanceCalculationPojo seekerLocation = new DistanceCalculationPojo();
            providerLocation.setLatitude(serviceRequest.getRider().getProvider().getLocationLatitude());
            providerLocation.setLongitude(serviceRequest.getRider().getProvider().getLocationLongitude());
            seekerLocation.setLatitude(serviceRequest.getServiceRequestLatitude());
            seekerLocation.setLongitude(serviceRequest.getServiceRequestLongitude());
            Double distance = locationUtil.calculateVincentyDistance(providerLocation, seekerLocation);
            int price = distance < 1000 ? 50 : (int) Math.ceil(distance / 1000) * 50;
            serviceRequestDetailProjection.setPrice("Rs. "+price);
            serviceRequestDetailProjection.setEstimatedDistance(distance);
            serviceRequestDetailProjection.setSeekerName(serviceRequest.getSeeker().getUser().getFullName());
            serviceRequestDetailProjection.setServiceType(ServiceType.valueOf(serviceRequest.getServiceType().getType()));
            serviceRequestDetailProjection.setVehicleType(serviceRequest.getVehicleType());
            serviceRequestDetailProjection.setMessage(serviceRequest.getServiceDescriptionByUser());
            return serviceRequestDetailProjection;

    }



}
