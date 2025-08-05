package com.tow.mandu.service.impl;

import com.tow.mandu.config.rabbitmq.MessageProducer;
import com.tow.mandu.enums.RiderStatus;
import com.tow.mandu.enums.RoleType;
import com.tow.mandu.model.Provider;
import com.tow.mandu.model.Rider;
import com.tow.mandu.model.ServiceType;
import com.tow.mandu.model.User;
import com.tow.mandu.pojo.EmailPojo;
import com.tow.mandu.pojo.RiderPojo;
import com.tow.mandu.repository.RiderRepository;
import com.tow.mandu.repository.ServiceTypeRepository;
import com.tow.mandu.repository.UserRepository;
import com.tow.mandu.service.RiderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final RiderRepository riderRepository;
    private final MessageProducer messageProducer;

    @Override
    @Transactional
    public RiderPojo save(RiderPojo riderPojo, Provider provider) {
        Rider rider = new Rider();
        User user = new User();
        user.setEmail(riderPojo.getEmail());
        user.setRole(RoleType.RIDER);
        user.setPhone(riderPojo.getPhone());
        user.setFullName(riderPojo.getFullName());
        String random = Stream.of("A", "a", "1", "@").map(s -> s +
                UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9@]", "").substring(0, 2)).reduce("", String::concat).substring(0, 10);
        user.setPassword(encoder.encode(random));
        List<ServiceType> serviceTypeEntities = riderPojo.getServices()
                .stream()
                .map(enumService -> serviceTypeRepository.findServiceTypeByType(enumService.name()))
                .collect(Collectors.toList());
        rider.setServiceType(serviceTypeEntities);
        rider.setStatus(RiderStatus.ACTIVE);
        rider.setProvider(provider);
        userRepository.save(user);
        rider.setUser(user);
        riderRepository.save(rider);
        messageProducer.convertAndSendGeneratedPasswordMail(new EmailPojo(
                rider.getUser().getEmail(),
                "Your Mandu Rider Account Credentials",
                "Your Mandu Rider account has been created successfully. " +
                        "Here are your credentials:\n\n" +
                        "Email: " + rider.getUser().getEmail() + "\n" +
                        "Password: " + random + "\n\n" +
                        "Please download Towmandu Rider App to login and start using your account.\n",
                0
        ));
        return riderPojo;
    }
}
