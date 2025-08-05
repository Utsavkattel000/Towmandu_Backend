package com.tow.mandu.controller;

import com.tow.mandu.pojo.UserPojoForLogin;
import com.tow.mandu.service.ProviderService;
import com.tow.mandu.service.ServiceRequestService;
import com.tow.mandu.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import static com.tow.mandu.enums.RoleType.ADMIN;
import static com.tow.mandu.enums.RoleType.PROVIDER;

@RequiredArgsConstructor
@Controller
public class AdminController {

    private final UserService userService;
    private final ServiceRequestService serviceRequestService;
    private final ProviderService providerService;


    @GetMapping({"/login", "/"})
    public String login() {

        return "admin-login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute UserPojoForLogin admin, HttpSession session, Model model) {
        if (admin.getEmail() == null || admin.getPassword() == null) {
            model.addAttribute("error", "Email and password cannot be null");
            return "admin-login";
        }
        if(userService.getUserByEmail(admin.getEmail()).getRole().equals(PROVIDER)) {
            if (userService.loginProvider(admin)) {
                session.setAttribute(PROVIDER.name(), userService.getUserByEmail(admin.getEmail()));
                return "redirect:/provider-dashboard";
            }
            model.addAttribute("error", "Invalid email or password");
            return "admin-login";
        }
        if (userService.loginAdmin(admin)) {
            session.setAttribute(ADMIN.name(), userService.getUserByEmail(admin.getEmail()));
            return "redirect:/admin-dashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
        }
        return "admin-login";
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (session.getAttribute(ADMIN.name()) == null) {
            redirectAttributes.addAttribute("error", "Please login first");
            return "redirect:/login";
        }
        model.addAttribute("adminDashboardData", userService.getAdminDashboardData(session));
        return "admin-dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }


    @GetMapping("/pending-providers")
    public String getPendingProviderData(HttpSession session, Model model, RedirectAttributes redirectAttributes) throws IOException {
        if (session.getAttribute(ADMIN.name()) == null) {
            redirectAttributes.addFlashAttribute("error", "Please login first");
            return "redirect:/login";
        }
        model.addAttribute("pendingProviders", providerService.getPendingProviderData());
        return "pending-providers";
    }

    @PatchMapping("/update-provider-status")
    public String approveProvider(@RequestParam Long id,
                                  HttpSession session,
                                  @RequestParam String status,
                                  RedirectAttributes redirectAttributes) {
        if (session.getAttribute(ADMIN.name()) == null) {
            redirectAttributes.addFlashAttribute("error", "Please login first");
            return "redirect:/login";
        }

        providerService.updateProviderStatus(id, status);
        return "redirect:/pending-providers";
    }

        @GetMapping("/view-all-requests")
    public String viewAllRequests(HttpSession session, Model model, RedirectAttributes redirectAttributes) throws IOException {
        if (session.getAttribute(ADMIN.name()) == null) {
            redirectAttributes.addFlashAttribute("error", "Please login first");
            return "redirect:/login";
        }
        model.addAttribute("allRequests", serviceRequestService.getAllServiceRequests());
        return "all-service-request";
    }



}
