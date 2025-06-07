package com.tow.mandu.restController;

import com.tow.mandu.enums.RoleType;
import com.tow.mandu.pojo.UserPojoForAndroid;
import com.tow.mandu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserPojoForAndroid user) {
        return ResponseEntity.ok().body(userService.save(user));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password, @RequestParam RoleType role) {
        return userService.login(email, password, role);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<?> isTokenValid(@RequestParam String token, @RequestParam String email, @RequestParam RoleType role) {
        return userService.isTokenValid(token, email,role);
    }




}
