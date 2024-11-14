package org.bibblan.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.net.http.HttpClient;

@Controller
public class NavigationController {

    @GetMapping(path = "/")
    public String home(){
        return "/home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

    @GetMapping(path = "/profile")
    public String profilePage() {
        return "/profile";
    }

    @GetMapping(path = "/register")
    public String showRegistrationForm() {
        return "/register";

    }

    @GetMapping(path="/terms-and-conditions")
    public String showTermsAndConditions(){
        return "/terms-and-conditions";
    }
}
