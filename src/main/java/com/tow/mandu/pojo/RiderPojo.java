package com.tow.mandu.pojo;

import com.tow.mandu.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RiderPojo {
    private String fullName;
    private String email;
    private String phone;
    private List<ServiceType> services;
}
