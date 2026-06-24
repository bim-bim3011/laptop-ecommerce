package com.se1906.laptopshop.service;

import com.se1906.laptopshop.dto.LoginRequest;
import com.se1906.laptopshop.dto.RegisterRequest;
import com.se1906.laptopshop.entity.User;

public interface AuthService {

       User login(LoginRequest request);

       void logout();

       User register(RegisterRequest request);

}
