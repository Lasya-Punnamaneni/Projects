package com.example.loginform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @Controller
    public class HomeController {

        @GetMapping("/home")
        public String redirectToW3Schools() {
            return "redirect:https://www.w3schools.com/";
        }

        @GetMapping("/login")
        public String loginPage() {
            return "login";
        }

        @GetMapping("/home")
        public String homePage() {
            return "home";
        }
    }

}