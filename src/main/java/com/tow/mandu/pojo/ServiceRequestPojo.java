package com.tow.mandu.pojo;

import com.tow.mandu.enums.ServiceStatus;
import com.tow.mandu.enums.ServiceType;
import com.tow.mandu.enums.VehicleType;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(hidden = true)
    private ServiceStatus status;
}
