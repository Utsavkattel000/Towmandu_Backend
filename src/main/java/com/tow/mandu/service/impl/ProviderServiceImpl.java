package com.tow.mandu.service.impl;

import com.tow.mandu.enums.ApproveStatus;
import com.tow.mandu.enums.RoleType;
import com.tow.mandu.model.Provider;
import com.tow.mandu.model.Review;
import com.tow.mandu.model.User;
import com.tow.mandu.pojo.BusinessPojo;
import com.tow.mandu.pojo.DistanceCalculationPojo;
import com.tow.mandu.projection.BusinessProjection;
import com.tow.mandu.projection.PendingProviderProjectionForAdmin;
import com.tow.mandu.repository.ProviderRepository;
import com.tow.mandu.repository.ReviewRepository;
import com.tow.mandu.repository.ServiceTypeRepository;
import com.tow.mandu.repository.UserRepository;
import com.tow.mandu.service.FileService;
import com.tow.mandu.service.ProviderService;
import com.tow.mandu.utils.LocationUtil;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.tow.mandu.enums.ApproveStatus.PENDING;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final LocationUtil locationUtil;
    private final ReviewRepository reviewRepository;
    private final FileService fileService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Transactional
    @Override
    public void save(BusinessPojo business) throws IOException {
        User user = new User();
        user.setFullName(business.getName());
        user.setEmail(business.getEmail());
        user.setPhone(business.getPhone());
        user.setPassword(encoder.encode(business.getPassword()));
        user.setRole(RoleType.PROVIDER);
        userRepository.save(user);
        Provider provider = new Provider();
        provider.setUser(user);
        provider.setLocationLatitude(business.getLocationLatitude());
        provider.setLocationLongitude(business.getLocationLongitude());
        provider.setApproveStatus(PENDING);
        provider.setRegistrationNumber(business.getRegistrationNumber());
        provider.setPanNumber(business.getPanNumber());
        provider.setIsActive(false);
        provider.setGeoHash(locationUtil.getGeoHash(business.getLocationLatitude(), business.getLocationLongitude()));
        provider.setCertificate(fileService.saveFileToDatabase(business.getCertificate()));
        providerRepository.save(provider);
    }

    @Override
    public Provider getByEmail(String email) {
        User user = userRepository.getByEmail(email);
        return providerRepository.findByUser(user).orElse(null);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void updateProviderStatus(Long id, String status) {
        Optional<Provider> providerOpt = providerRepository.findById(id);
        if (providerOpt.isPresent()) {
            Provider provider = providerOpt.get();
            provider.setApproveStatus(ApproveStatus.valueOf(status));
            providerRepository.save(provider);
        }
    }

    @Override
    public List<PendingProviderProjectionForAdmin> getPendingProviderData() {
        List<Provider> pendingProviders = providerRepository.findByApproveStatusNot(ApproveStatus.APPROVED);
        List<PendingProviderProjectionForAdmin> pendingProviderProjectionForAdmins = new ArrayList<>();

        for (Provider provider : pendingProviders) {
            PendingProviderProjectionForAdmin pendingProviderProjection = new PendingProviderProjectionForAdmin();
            pendingProviderProjection.setId(provider.getId());
            pendingProviderProjection.setRegisteredName(provider.getUser().getFullName());
            pendingProviderProjection.setEmail(provider.getUser().getEmail());

            String placeName = locationUtil.getPlaceName(provider.getLocationLatitude(), provider.getLocationLongitude());
            pendingProviderProjection.setLocation(placeName);

            pendingProviderProjection.setLocationLink(
                    locationUtil.createGoogleMapsRedirectLink(
                            provider.getLocationLatitude(), provider.getLocationLongitude()));
            pendingProviderProjection.setApprovalStatus(provider.getApproveStatus().name());
            pendingProviderProjectionForAdmins.add(pendingProviderProjection);
        }

        return pendingProviderProjectionForAdmins;
    }

    @Override
    public List<BusinessProjection> getNearestProvidersByLocation(Double latitude, Double longitude) {
        List<String> geohashes = locationUtil.getGeoHashWithNeighbors(latitude, longitude);
        List<BusinessProjection> candidates = providerRepository.findTop30ByGeoHashes(geohashes);

        for (BusinessProjection candidate : candidates) {
            DistanceCalculationPojo providerLocation = new DistanceCalculationPojo();
            DistanceCalculationPojo seekerLocation = new DistanceCalculationPojo();
            providerLocation.setLatitude(candidate.getLocationLatitude());
            providerLocation.setLongitude(candidate.getLocationLongitude());
            seekerLocation.setLatitude(latitude);
            seekerLocation.setLongitude(longitude);

            Double distance = locationUtil.calculateVincentyDistance(providerLocation, seekerLocation);
            candidate.setEstimatedDistance(distance);
            candidate.setRating(calculateRating(candidate.getId()));
        }
        candidates.sort(Comparator.comparingDouble(BusinessProjection::getEstimatedDistance));
        int rankLimit = Math.min(20, candidates.size());
        for (int i = 0; i < rankLimit; i++) {
            candidates.get(i).setRank(i);
        }
        return candidates.subList(0, rankLimit);
    }

    private Double calculateRating(Long providerId) {
        double totalRating = 0;
        List<Review> reviews = reviewRepository.findByProviderId(providerId);
        if (reviews != null && !reviews.isEmpty()) {
            double allRating = reviews.stream().mapToDouble(Review::getRating).sum();
            totalRating = allRating / reviews.size();
        }
        return totalRating;
    }
}