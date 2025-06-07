package com.tow.mandu.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminDashboardProjection {
    private String fullName;
    private Long totalUsers;
    private Long totalProviders;
    private Long totalPendingProviders;
    private Long activeProviders;

    public AdminDashboardProjection(Long totalUsers, Long totalProviders, Long totalPendingProviders, Long activeProviders) {
        this.totalUsers = totalUsers;
        this.totalProviders = totalProviders;
        this.totalPendingProviders = totalPendingProviders;
        this.activeProviders = activeProviders;
    }
}
