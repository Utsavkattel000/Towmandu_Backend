package com.tow.mandu.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Business {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private String phone;
	private String phone2;
	private String locationLongitude;
	private String locationLattitude;
	private boolean approveStatus;
	@OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ServiceVehicleType> serviceVehicleTypes;
	@OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
	
	
}
