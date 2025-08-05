package com.tow.mandu.auth;

import java.io.IOException;
import java.util.List;

import com.tow.mandu.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tow.mandu.utils.AppConstants;
import com.tow.mandu.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader(AppConstants.AUTHORIZATION);

        if (header != null && header.startsWith(AppConstants.BEARER)) {
            String token = header.substring(AppConstants.BEARER.length());
            String email = jwtUtil.getEmail(token);
            String role = jwtUtil.getRole(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (role != null) {
                    RoleType roleEnum = null;
                    try {
                        roleEnum = RoleType.valueOf(role);
                    } catch (IllegalArgumentException ignored) {

                    }

                    if (roleEnum != null && jwtUtil.validateToken(token, email, roleEnum)) {
                        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                email, null, List.of(authority));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
