package com.tow.mandu.service.impl;

import com.tow.mandu.enums.RoleType;
import com.tow.mandu.model.Seeker;
import com.tow.mandu.model.User;
import com.tow.mandu.pojo.AdminPojoForLogin;
import com.tow.mandu.pojo.UserPojoForAndroid;
import com.tow.mandu.projection.AdminDashboardProjection;
import com.tow.mandu.repository.SeekerRepository;
import com.tow.mandu.repository.UserRepository;
import com.tow.mandu.service.UserService;
import com.tow.mandu.utils.JwtUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.tow.mandu.enums.RoleType.ADMIN;
import static com.tow.mandu.enums.RoleType.SEEKER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;

    private final JwtUtil jwt;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final SeekerRepository seekerRepository;

    @Transactional
    @Override
    public Map<String, String> save(UserPojoForAndroid user) {
        Map<String, String> response = new HashMap<>();
        if (user.getFullName() == null || user.getFullName().trim().isEmpty() ||
                user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty() ||
                user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            response.put("message", "Can't be null");
            return response;
        }
        if (userRepo.existsByEmail(user.getEmail())) {
            response.put("message", "Email already exists");
            return response;
        }
        user.setPassword(encoder.encode(user.getPassword()));
        User user1 = new User();
        user1.setFullName(user.getFullName());
        user1.setEmail(user.getEmail().toLowerCase());
        user1.setPassword(user.getPassword());
        user1.setPhone(user.getPhone());
        user1.setRole(SEEKER);
        Seeker seeker = new Seeker();
        seeker.setUser(user1);
        seekerRepository.save(seeker);
        userRepo.save(user1);
        response.put("message", "Signup successful.");
        return response;
    }

    @Override
    public ResponseEntity<?> login(String email, String password, RoleType role) {
        if (Objects.isNull(email) || Objects.isNull(password)) {
            return ResponseEntity.badRequest().body("Can't be null");
        }

        User user = userRepo.getByEmail(email.toLowerCase());
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        if (encoder.matches(password, user.getPassword()) && user.getRole().equals(role)) {
            String token = jwt.generateToken(email.toLowerCase(), user.getRole().name());
            return ResponseEntity.ok().body(token);
        }
        if (encoder.matches(password, user.getPassword()) && !user.getRole().equals(role)) {
            return ResponseEntity.badRequest().body("You are not authorized for this Place.Please login in valid app.");
        }

        return ResponseEntity.badRequest().body("Incorrect password");

    }

    @Override
    public ResponseEntity<?> isTokenValid(String token, String email, RoleType role) {
        boolean isValid = jwt.validateToken(token, email, role);
        if (isValid) {
            return ResponseEntity.ok("Token is valid.");
        } else {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }

    @Override
    public Boolean loginAdmin(AdminPojoForLogin adminPojoForLogin) {
        if (adminPojoForLogin.getEmail() == null || adminPojoForLogin.getPassword() == null) {
            return false;
        }
        User user = userRepo.getByEmail(adminPojoForLogin.getEmail());
        if (user != null && encoder.matches(adminPojoForLogin.getPassword(), user.getPassword())) {
            return user.getRole().equals(ADMIN);
        }
        return false;
    }

    @Override
    public AdminDashboardProjection getAdminDashboardData(HttpSession session) {
        AdminDashboardProjection adminDashboardProjection = userRepo.getAdminDashboardData();
        adminDashboardProjection.setFullName(getUserByEmail(((User) session.getAttribute("admin")).getEmail()).getFullName());
        return adminDashboardProjection;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.getByEmail(email);
    }


}
