package com.tow.mandu.model;

import com.tow.mandu.enums.ServiceStatus;
import com.tow.mandu.enums.VehicleType;
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
	@Column(length = 255)
	private ServiceStatus serviceStatus;
	private LocalDateTime acceptTime;
	private LocalDateTime serviceCompletionTime;
	private Double serviceRequestLongitude;
	private Double serviceRequestLatitude;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "service_type_id", nullable = false)
	private ServiceType serviceType;
	@Enumerated(EnumType.STRING)
	private VehicleType vehicleType;
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