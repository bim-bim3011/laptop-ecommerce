package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.dto.LoginRequest;
import com.se1906.laptopshop.dto.RegisterRequest;
import com.se1906.laptopshop.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {

      AuthService authService;

    @GetMapping({"/","/login"})
    public String loginPage(){
        return "login";
    }


    @PostMapping("/login")
    public String login(HttpSession session, Model model,
                        @Valid @RequestBody LoginRequest request,
                        BindingResult result,
                        RedirectAttributes redirect ){


        return "redirect:/home-page";
    }


    @PostMapping("/logout")
    public String logout(HttpSession session){


        return "redirect:/home-page";

    }

    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }


    @PostMapping("/register")
    public String doRegister(HttpSession session , Model model,
                             @Valid @RequestBody RegisterRequest request,
                             BindingResult result,
                             RedirectAttributes redirect){



        return "redirect:/home-page";
    }
}
