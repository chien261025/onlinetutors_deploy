package com.example.onlinetutors.controller;

import com.example.onlinetutors.model.Email;
import com.example.onlinetutors.service.impl.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Slf4j(topic = "EmailController")
@Controller
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-email")
    public String sendEmail(Model model, @Valid @ModelAttribute("email")Email email) {
        log.info("Sending email...");
        emailService.sendEmail(email.getTo(), email.getSubject(), email.getBody());
        return "redirect:/email";
    }




}
