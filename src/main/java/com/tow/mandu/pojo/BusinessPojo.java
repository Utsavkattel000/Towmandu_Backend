package com.tow.mandu.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessPojo {
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String phone;
    @NotNull
    private Double locationLongitude;
    @NotNull
    private Double locationLatitude;
    @NotNull
    private String panNumber;
    @NotNull
    private String registrationNumber;
    @JsonIgnore
    private MultipartFile certificate;

}
