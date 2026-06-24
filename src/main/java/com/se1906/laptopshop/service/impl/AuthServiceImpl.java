package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.dto.LoginRequest;
import com.se1906.laptopshop.dto.RegisterRequest;
import com.se1906.laptopshop.entity.User;
import com.se1906.laptopshop.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    PasswordEncoder passwordEncoder ;

    @Override
    public User login(LoginRequest request) {
        return null;
    }

    @Override
    public void logout() {

    }

    @Override
    public User register(RegisterRequest request) {
        return null;
    }
}
