package com.se1906.laptopshop.controller;

import com.se1906.laptopshop.dto.LoginRequest;
import com.se1906.laptopshop.dto.RegisterRequest;
import com.se1906.laptopshop.entity.User;
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
import org.springframework.web.bind.annotation.ModelAttribute;
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
                        @Valid @ModelAttribute LoginRequest request,
                        BindingResult result,
                        RedirectAttributes redirect ){

        if(result.hasErrors()){
            return "login";
        }
        
        try {
            User user = authService.login(request);
            System.out.println(user);
            if(user != null){
                session.setAttribute("user", user);
                return "redirect:/home-page";
            } else {
                model.addAttribute("error", "Invalid email or password.");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Invalid email or password.");
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirect){
        session.removeAttribute("user");
        session.invalidate();
        redirect.addFlashAttribute("success", "You have been logged out successfully.");
        return "redirect:/auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }


    @PostMapping("/register")
    public String doRegister(HttpSession session , Model model,
                             @Valid @ModelAttribute("registerRequest") RegisterRequest request,
                             BindingResult result,
                             RedirectAttributes redirect){

        if (result.hasErrors()) {
            return "register";
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }

        try {
            authService.register(request);
            redirect.addFlashAttribute("success", "Registration successful. Please login.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during registration. Please try again.");
            return "register";
        }
    }
}
