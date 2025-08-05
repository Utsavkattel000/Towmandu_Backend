package com.tow.mandu.controller;

import com.tow.mandu.model.Provider;
import com.tow.mandu.model.User;
import com.tow.mandu.pojo.BusinessPojo;
import com.tow.mandu.pojo.RiderPojo;
import com.tow.mandu.service.ProviderService;
import com.tow.mandu.service.RiderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

import static com.tow.mandu.enums.RoleType.PROVIDER;

@Controller
@RequiredArgsConstructor
public class BusinessController {


    private final ProviderService providerService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RiderService riderService;

    @GetMapping("/business-registration")
    public String getBusinessRegistrationPage() {

        return "provider-signup";
    }


    @PostMapping(value = "/business-registration", consumes = "multipart/form-data")
    public String saveBusiness(@ModelAttribute BusinessPojo businessPojo) throws IOException {
        providerService.save(businessPojo);
        return "redirect:/login";
    }

    @GetMapping("/provider-dashboard")
    public String getProviderDashboard(HttpSession session, Model model) {
        if (session.getAttribute(PROVIDER.name()) == null) {
            return "redirect:/login";
        }
        Provider provider = providerService.getByEmail(((User) session.getAttribute(PROVIDER.name())).getEmail());
        model.addAttribute("providerDashboardData", providerService.getProviderDashboardDataById(provider.getId()));
        return "provider-dashboard";
    }

    @GetMapping("add-rider")
    public String getAddRiderPage(HttpSession session, Model model) {
        if (session.getAttribute(PROVIDER.name()) == null) {
            return "redirect:/login";
        }
        Provider provider = providerService.getByEmail(((User) session.getAttribute(PROVIDER.name())).getEmail());
        model.addAttribute("provider", provider);
        return "add-rider";
    }

    @PostMapping("/add-rider")
    public String saveRider(@ModelAttribute("rider") RiderPojo rider, HttpSession session) {
        if (session.getAttribute(PROVIDER.name()) == null) {
            return "redirect:/login";
        }
        Provider provider = providerService.getByEmail(((User) session.getAttribute(PROVIDER.name())).getEmail());
        riderService.save(rider, provider);
        return "redirect:/provider-dashboard";
    }
}
