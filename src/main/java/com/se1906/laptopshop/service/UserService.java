package com.se1906.laptopshop.service;

import com.se1906.laptopshop.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(int id);
    User updateUser(int id, User userDetails);
    void deleteUser(int id);
    Page<User> getPaginatedUsers(String keyword, String status, int pageNo, int pageSize, String sortField, String sortDir);
}
