package com.tow.mandu.service.impl;

import com.tow.mandu.model.ServiceType;
import com.tow.mandu.repository.ServiceTypeRepository;
import com.tow.mandu.service.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceTypeServiceImpl implements ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;



}
