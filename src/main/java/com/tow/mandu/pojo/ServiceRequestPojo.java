package com.tow.mandu.pojo;

import com.tow.mandu.enums.ServiceType;
import com.tow.mandu.enums.VehicleType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestPojo {
    private Double longitude;
    private Double latitude;
    private VehicleType vehicleType;
    private ServiceType serviceType;
    private String description;
}
