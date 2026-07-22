package com.se1906.laptopshop.service;

import com.se1906.laptopshop.entity.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(int id);
    User updateUser(int id, User userDetails);
    void deleteUser(int id);
}
