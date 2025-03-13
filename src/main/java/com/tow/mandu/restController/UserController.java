package com.tow.mandu.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tow.mandu.model.User;
import com.tow.mandu.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	UserService userService;
	
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody User user){
		if(user.getFullName()!=null && (user.getEmail()!=null &&
				user.getPassword()!=null && user.getPhone()!=null
				)) {
			userService.save(user);
			return ResponseEntity.ok("\"success\"");
		}
		
		
		
		return ResponseEntity.badRequest().body("\"failed\"");
	}
	
	
	
}
