package com.tow.mandu.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tow.mandu.model.User;
import com.tow.mandu.service.UserService;

import jakarta.servlet.http.HttpSession;

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
	
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody User user,HttpSession session){
		if(!user.getEmail().isEmpty() && !user.getPassword().isEmpty()) {
			if(userService.getByEmailAndPassword(user.getEmail(), user.getPassword())!=null) {
				session.setAttribute("user",userService.getByEmailAndPassword(user.getEmail(), user.getPassword()));
				return ResponseEntity.ok("\"success\"");
			}
			return ResponseEntity.badRequest().body("\" User does not exist\"");
		}
		return ResponseEntity.badRequest().body("\"failed\"");
	}
	
	
}
