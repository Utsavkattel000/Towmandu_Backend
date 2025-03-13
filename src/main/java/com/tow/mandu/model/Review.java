package com.tow.mandu.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private byte rating;
	private String review;
	@ManyToOne
	@JoinColumn(name = "business_id")
	private Business business;
	@ManyToOne
	@JoinColumn(name="customer_id")
	private User user;
	
}
