package com.tow.mandu.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPojoForAndroid {
    private String fullName;
    private String phone;
    private String email;
    private String password;
}
