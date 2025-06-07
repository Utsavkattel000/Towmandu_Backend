package com.tow.mandu.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PendingProviderProjectionForAdmin {
    private Long id;
    private String registeredName;
    private List<String> phone;
    private String email;
    private Map<String, Integer> serviceTypeAndDriverCount;
    private String location;
    private String locationLink;
    private String documentLink;
    private String approvalStatus;
}
