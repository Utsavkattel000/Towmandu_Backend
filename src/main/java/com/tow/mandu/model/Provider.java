package com.tow.mandu.model;

import com.tow.mandu.enums.ApproveStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(name = "provider_geohash_index", columnList = "geoHash")
})
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Double locationLongitude;
    private Double locationLatitude;
    private String panNumber;
    private String registrationNumber;
    @Enumerated(EnumType.STRING)
    private ApproveStatus approveStatus;
    private Boolean isActive;
    @ManyToOne
    @JoinColumn(name = "updated_by_user_id")
    private User lastUpdatedBy;
    private String geoHash;
    @OneToOne(cascade = CascadeType.ALL)
    private File certificate;
}