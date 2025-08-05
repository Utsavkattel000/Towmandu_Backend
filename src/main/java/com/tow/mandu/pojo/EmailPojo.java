package com.tow.mandu.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailPojo {
    String to;
    String subject;
    String body;
    int retryCount = 0;
}
