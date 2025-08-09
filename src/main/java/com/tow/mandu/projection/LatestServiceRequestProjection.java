package com.tow.mandu.projection;

import com.tow.mandu.enums.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LatestServiceRequestProjection {
    private Long id;
    private ServiceStatus status;
    private String providerName;
    private Double latitude;
    private Double longitude;
    private Double estimatedDistance;
    private String phone;
    private String riderName;
    private String riderPhone;
}
