package com.tow.mandu.service;

import com.tow.mandu.pojo.ServiceRequestPojo;
import com.tow.mandu.projection.AllServiceRequestProjection;

import java.io.IOException;
import java.util.List;

public interface ServiceRequestService {

    List<AllServiceRequestProjection> getAllServiceRequests() throws IOException;

    void save(ServiceRequestPojo serviceRequestPojo);

    Boolean hasPendingRequest();
}
