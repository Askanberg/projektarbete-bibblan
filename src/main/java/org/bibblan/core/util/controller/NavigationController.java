package org.bibblan.core.util.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    @GetMapping(path="/")
    public String redirectToHome(){
        return "home";
    }

    @GetMapping(path = "/home")
    public String home(){return "home";}

    @GetMapping(path="/terms-and-conditions")
    public String showTermsAndConditions(){
        return "terms-and-conditions";
    }

    @GetMapping(path = "/profile")
    public String redirectToProfile() {
        return "profile";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping(path = "/register")
    public String showRegistrationForm() {
        return "register";

    }

}
