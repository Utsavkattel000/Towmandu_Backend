package com.tow.mandu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class RiderAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "service_request_id")
    private ServiceRequest serviceRequest;
    @ManyToOne
    @JoinColumn(name = "rider_id")
    private Rider rider;
    private String status; // e.g., "ASSIGNED", "ACCEPTED", "REJECTED"
    private LocalDateTime assignedTime;
    private LocalDateTime responseTime;
}