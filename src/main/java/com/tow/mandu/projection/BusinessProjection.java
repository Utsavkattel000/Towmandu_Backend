package com.tow.mandu.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessProjection {
    private Long id;
    private String name;
    private Double rating;
    private Double estimatedDistance;
    private Double locationLongitude;
    private Double locationLatitude;
    private Integer rank;
    private String phone;
    private String price;

    public BusinessProjection(Long id, String name, Double locationLongitude, Double locationLatitude, String phone) {
        this.id = id;
        this.name = name;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.phone = phone;
    }
}
