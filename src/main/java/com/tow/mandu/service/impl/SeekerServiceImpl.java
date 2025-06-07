package com.tow.mandu.service.impl;

import com.tow.mandu.model.Seeker;
import com.tow.mandu.model.User;
import com.tow.mandu.repository.SeekerRepository;
import com.tow.mandu.service.SeekerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeekerServiceImpl implements SeekerService {

    private final SeekerRepository seekerRepository;

    @Override
    public Seeker getSeekerByUser(User user){
       return seekerRepository.findByUser(user);
    }

}
