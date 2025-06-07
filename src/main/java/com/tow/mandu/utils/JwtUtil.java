package com.tow.mandu.utils;

import com.tow.mandu.config.JwtConfig;
import com.tow.mandu.enums.RoleType;
import com.tow.mandu.model.User;
import com.tow.mandu.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    public String generateToken(String email, String role) {
        if (email == null || role == null) {
            throw new IllegalArgumentException("Can't be null");
        }
        return Jwts.builder().setSubject(email).claim("role", role).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    public String getEmail(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public String getRole(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody()
                    .get("role", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validateToken(String token, String email, RoleType roleType) {
        String tokenEmail = getEmail(token);
        return (tokenEmail != null && tokenEmail.equals(email) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public Date extractExpiration(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody()
                .getExpiration();
    }

    public User getUserFromToken() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr == null) return null;

        HttpServletRequest req = attr.getRequest();
        final String header = req.getHeader(AppConstants.AUTHORIZATION);

        if (header != null && header.startsWith(AppConstants.BEARER)) {
            String token = header.substring(7);
            String email = getEmail(token);
            String role = getRole(token);

            if (email != null && role != null && validateToken(token, email, RoleType.valueOf(role))) {
                return userRepository.getByEmail(email.toLowerCase());
            }
        }
        return null;
    }

}
