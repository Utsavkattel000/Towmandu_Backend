package com.tow.mandu.restController;

import com.tow.mandu.pojo.ServiceRequestPojo;
import com.tow.mandu.service.ProviderService;
import com.tow.mandu.service.ServiceRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceRequestService serviceRequestService;
    private final ProviderService providerService;

    @Transactional
    @PostMapping("/request-service")
    public ResponseEntity<?> requestService(@RequestBody ServiceRequestPojo serviceRequest) {
        if (serviceRequestService.hasPendingRequest()) {
            return ResponseEntity.badRequest().body("You already have a pending request");
        }
        serviceRequestService.save(serviceRequest);
        return ResponseEntity.ok().body(providerService.getNearestProvidersByLocation(serviceRequest.getLatitude(), serviceRequest.getLongitude()));
    }
}
