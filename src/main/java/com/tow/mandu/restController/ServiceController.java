package com.tow.mandu.restController;

import com.tow.mandu.enums.RoleType;
import com.tow.mandu.enums.ServiceStatus;
import com.tow.mandu.pojo.ServiceRequestPojo;
import com.tow.mandu.projection.BusinessProjection;
import com.tow.mandu.service.EsewaPaymentService;
import com.tow.mandu.service.SeekerService;
import com.tow.mandu.service.ServiceRequestService;
import com.tow.mandu.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceRequestService serviceRequestService;
    private final JwtUtil jwtUtil;
    private final SeekerService seekerService;
    private final EsewaPaymentService esewaPaymentService;

    @PostMapping("/request-service")
    public ResponseEntity<?> requestService(@RequestBody ServiceRequestPojo serviceRequest) {
        if (serviceRequestService.hasPendingRequest()) {
            return ResponseEntity.badRequest().body("You already have a pending request");
        }
        List<BusinessProjection> businessProjections = serviceRequestService.save(serviceRequest);
        if(businessProjections==null || businessProjections.isEmpty()) {
            return ResponseEntity.badRequest().body("No providers available in your area");
        }
        return ResponseEntity.ok().body(businessProjections);
    }

    @PatchMapping("/select-provider/{providerId}")
    public ResponseEntity<?> selectProvider(@PathVariable("providerId") Long providerId) {
        Long serviceRequestId = serviceRequestService.getCurrentServiceRequestId();
        serviceRequestService.selectProvider(serviceRequestId, providerId);
        return ResponseEntity.ok().body("Service Request Sent to Provider");
    }

    @GetMapping("/get-latest-request")
    public ResponseEntity<?> getLatestServiceRequest() {

        return ResponseEntity.ok().body(serviceRequestService.getLatestServiceRequestForSeeker(seekerService.getSeekerByUser(jwtUtil.getUserFromToken()).getId()));
    }

    @GetMapping("/get-requests")
    public ResponseEntity<?> getServiceRequests() {
        return ResponseEntity.ok().body(serviceRequestService.getRiderServiceRequest());
    }

    @PatchMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestParam ServiceStatus serviceStatus, @RequestParam Long id) {
        if(serviceStatus == null) {
            return ResponseEntity.badRequest().body("Service status cannot be null");
        }
        if(!jwtUtil.getUserFromToken().getRole().equals(RoleType.RIDER)) {
            return ResponseEntity.status(403).body("Unauthorized access");
        }
        if (serviceRequestService.updateStatus(id, serviceStatus)) {
            return ResponseEntity.ok().body("Request has been "+serviceStatus.name()+" successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update service request status");
        }
    }

    @GetMapping("/getRequestDetail")
    public ResponseEntity<?> getServiceRequestsDetails(@RequestParam Long id) {
        return ResponseEntity.ok().body(serviceRequestService.getRiderServiceRequestDetails(id));
    }

    @PostMapping("/request-payment-otp")
    public ResponseEntity<?> requestPaymentOtp(@RequestParam String email, @RequestParam String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body("Email and password cannot be empty");
        }
        esewaPaymentService.sendOTP(email, password);
        return ResponseEntity.ok().body("OTP sent to your email");
    }
}
