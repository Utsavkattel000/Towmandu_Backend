package com.tow.mandu.service;

import com.tow.mandu.enums.RoleType;
import com.tow.mandu.pojo.AdminPojoForLogin;
import com.tow.mandu.pojo.UserPojoForAndroid;
import com.tow.mandu.projection.AdminDashboardProjection;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

import com.tow.mandu.model.User;

import java.util.Map;

public interface UserService {

	Map<String, String> save(UserPojoForAndroid user);
	
	ResponseEntity<?> login(String email, String password, RoleType roleType);
	
	ResponseEntity<?> isTokenValid(String token, String email, RoleType roleType);

	Boolean loginAdmin(AdminPojoForLogin adminPojoForLogin);


	AdminDashboardProjection getAdminDashboardData(HttpSession session);

	User getUserByEmail(String email);
}
