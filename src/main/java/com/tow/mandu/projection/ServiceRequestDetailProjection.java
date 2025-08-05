package com.tow.mandu.projection;

import com.tow.mandu.enums.ServiceType;
import com.tow.mandu.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestDetailProjection {
    private Long id;
    private String seekerName;
    private String phone;
    private String price;
    private Double locationLatitude;
    private Double locationLongitude;
    private Double estimatedDistance;
    private VehicleType vehicleType;
    private ServiceType serviceType;
    private String message;
}
