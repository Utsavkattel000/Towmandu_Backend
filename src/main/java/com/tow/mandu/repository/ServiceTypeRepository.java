package com.tow.mandu.repository;

import com.tow.mandu.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {

    ServiceType findServiceTypeByType(String serviceType);

}
