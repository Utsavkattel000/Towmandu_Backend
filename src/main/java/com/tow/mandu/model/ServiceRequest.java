package com.tow.mandu.model;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ServiceRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String serviceDescriptionByUser;
	private LocalTime requestTime;
	private LocalTime acceptTime;
	private LocalTime serviceCompletionTime;
	private String serviceRequestLongitude;
	private String serviceRequestLatitude;
	private String distanceToSelectedBusiness;
	private float price;
	
	
	
}
