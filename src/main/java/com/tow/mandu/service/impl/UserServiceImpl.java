package com.tow.mandu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tow.mandu.model.User;
import com.tow.mandu.repository.UserRepository;
import com.tow.mandu.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UserRepository userRepo;

	@Override
	public void save(User user) {
		userRepo.save(user);
		
	}

	
}
