package com.tow.mandu.service;

import com.tow.mandu.model.User;

public interface UserService {
	
	void save(User user);
	
	User getByEmailAndPassword(String email, String password);

}
