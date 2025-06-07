package com.tow.mandu.service;

import com.tow.mandu.model.Provider;
import com.tow.mandu.pojo.BusinessPojo;
import com.tow.mandu.projection.BusinessProjection;
import com.tow.mandu.projection.PendingProviderProjectionForAdmin;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.List;

public interface ProviderService {


	@Transactional
	void save(BusinessPojo business) throws IOException;

	Provider getByEmail(String email);

	Boolean existsByEmail(String email);

	void updateProviderStatus(Long id, String status);

	List<PendingProviderProjectionForAdmin> getPendingProviderData() throws IOException;

	List<BusinessProjection> getNearestProvidersByLocation(Double latitude, Double longitude);
}
