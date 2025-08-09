package com.tow.mandu.service;

import com.tow.mandu.enums.ServiceStatus;
import com.tow.mandu.pojo.ServiceRequestPojo;
import com.tow.mandu.projection.AllServiceRequestProjection;
import com.tow.mandu.projection.BusinessProjection;
import com.tow.mandu.projection.LatestServiceRequestProjection;
import com.tow.mandu.projection.ServiceRequestDetailProjection;

import java.io.IOException;
import java.util.List;

public interface ServiceRequestService {

    List<AllServiceRequestProjection> getAllServiceRequests() throws IOException;

    List<BusinessProjection> save(ServiceRequestPojo serviceRequestPojo);

    Boolean hasPendingRequest();

    Boolean updateStatus(Long serviceRequestId, ServiceStatus status);

    LatestServiceRequestProjection getLatestServiceRequestForSeeker(Long seekerId);

    Boolean selectProvider(Long serviceRequestId, Long providerId);

    Long getCurrentServiceRequestId();

    List<ServiceRequestDetailProjection> getRiderServiceRequest();

    ServiceRequestDetailProjection getRiderServiceRequestDetails(Long Id);
}
