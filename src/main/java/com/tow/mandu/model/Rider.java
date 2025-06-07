package com.tow.mandu.model;

import com.tow.mandu.enums.RiderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Rider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<ServiceType> serviceType;
    @Enumerated(EnumType.STRING)
    private RiderStatus status;
    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;
}