package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.entity.User;
import com.se1906.laptopshop.repository.UserRepository;
import com.se1906.laptopshop.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public User updateUser(int id, User userDetails) {
        User existing = getUserById(id);
        existing.setFullName(userDetails.getFullName());
        existing.setPhoneNumber(userDetails.getPhoneNumber());
        existing.setAddress(userDetails.getAddress());
        existing.setStatus(userDetails.getStatus());
        if (userDetails.getRoles() != null && !userDetails.getRoles().isEmpty()) {
            existing.setRoles(userDetails.getRoles());
        }
        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(int id) {
        User existing = getUserById(id);
        userRepository.delete(existing);
    }
}
