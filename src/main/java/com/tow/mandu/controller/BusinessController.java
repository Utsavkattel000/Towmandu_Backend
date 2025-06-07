package com.tow.mandu.controller;

import com.tow.mandu.pojo.BusinessPojo;
import com.tow.mandu.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class BusinessController {


    private final ProviderService providerService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/business-registration")
    public String getBusinessRegistrationPage() {
        return "provider-signup";
    }


    @PostMapping(value = "/business-registration", consumes = "multipart/form-data")
    public String saveBusiness(@ModelAttribute BusinessPojo businessPojo) throws IOException {
        providerService.save(businessPojo);
        sendRiderNotification("aforapple", "HIII");
        return "redirect:/login";
    }
    public void sendRiderNotification(String riderEmail, Object notification) {
        simpMessagingTemplate.convertAndSend("topic/notifications"+riderEmail, notification);
    }
}
