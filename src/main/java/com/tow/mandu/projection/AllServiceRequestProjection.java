package com.tow.mandu.projection;

import com.tow.mandu.enums.ServiceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AllServiceRequestProjection {
    private Long id;
    private String providerName;
    private String seekerName;
    private String driverName;
    private LocalDateTime requestTime;
    private LocalDateTime acceptTime;
    private LocalDateTime serviceCompletionTime;
    private Double serviceRequestLongitude;
    private Double serviceRequestLatitude;
    private Double providerLongitude;
    private Double providerLatitude;
    private String serviceRequestLocation;
    private String serviceRequestLocationLink;
    private Double distance;
    private String paidAmount;
    private BigDecimal basePrice;
    private BigDecimal finalPrice;
    private String serviceType;
    private String serviceStatus;

    public AllServiceRequestProjection(
            Long id, String providerName, String seekerName,
            String driverName, LocalDateTime requestTime,
            LocalDateTime acceptTime, LocalDateTime serviceCompletionTime,
            Double serviceRequestLongitude, Double serviceRequestLatitude,
            Double providerLongitude, Double providerLatitude,
            Double distance, BigDecimal basePrice,
            BigDecimal finalPrice, String serviceType, ServiceStatus serviceStatus) {
        this.id = id;
        this.providerName = providerName;
        this.seekerName = seekerName;
        this.driverName = driverName;
        this.requestTime = requestTime;
        this.acceptTime = acceptTime;
        this.serviceCompletionTime = serviceCompletionTime;
        this.serviceRequestLongitude = serviceRequestLongitude;
        this.serviceRequestLatitude = serviceRequestLatitude;
        this.providerLongitude = providerLongitude;
        this.providerLatitude = providerLatitude;
        this.distance = distance;
        this.paidAmount = "Base Price: " + basePrice + ", Final Price: " + finalPrice;
        this.serviceType = serviceType;
        this.serviceStatus = serviceStatus.name();
    }
}
