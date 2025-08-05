package com.tow.mandu.service.impl;

import com.tow.mandu.enums.ServiceType;
import com.tow.mandu.repository.ServiceTypeRepository;
import com.tow.mandu.service.ServiceTypeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceTypeServiceImpl implements ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    @PostConstruct
    public void saveServiceType() {
        for (ServiceType type : ServiceType.values()) {
            boolean exists = serviceTypeRepository.existsByType(type.name());
            if (!exists) {
                com.tow.mandu.model.ServiceType entity = new com.tow.mandu.model.ServiceType();
                entity.setType(type.name());
                serviceTypeRepository.save(entity);
            }
        }
    }
}
