package com.tow.mandu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Byte rating;
	private String review;
	@ManyToOne
	@JoinColumn(name = "service_request_id")
	private ServiceRequest serviceRequest;
}