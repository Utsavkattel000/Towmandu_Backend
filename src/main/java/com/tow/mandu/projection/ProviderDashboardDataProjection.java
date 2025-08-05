package com.tow.mandu.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProviderDashboardDataProjection {
    private Long id;
    private String fullName;
    private Long onlineRiders;
    private Long totalRiders;
    private Long pendingRequests;
    private Long workOnProgress;
    private Long completedToday;
}
