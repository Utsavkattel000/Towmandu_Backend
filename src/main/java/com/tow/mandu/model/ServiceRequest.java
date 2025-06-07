package com.tow.mandu.model;

import com.tow.mandu.enums.ServiceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ServiceRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String serviceDescriptionByUser;
	private LocalDateTime requestTime;
	private String paymentStatus;
	@Enumerated(EnumType.STRING)
	private ServiceStatus serviceStatus;
	private LocalDateTime acceptTime;
	private LocalDateTime serviceCompletionTime;
	private Double serviceRequestLongitude;
	private Double serviceRequestLatitude;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service_type_id", nullable = false)
	private ServiceType serviceType;
	private BigDecimal basePrice;
	private BigDecimal finalPrice;
	private Double distance;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "seeker_id", nullable = false)
	private Seeker seeker;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rider_id")
	private Rider rider;
	private String riderDescription;
}