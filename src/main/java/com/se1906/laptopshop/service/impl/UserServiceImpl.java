package com.se1906.laptopshop.service.impl;

import com.se1906.laptopshop.entity.User;
import com.se1906.laptopshop.repository.UserRepository;
import com.se1906.laptopshop.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findActiveUsers();
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
        existing.setIsDeleted(true);
        userRepository.save(existing);
    }

    @Override
    public Page<User> getPaginatedUsers(String keyword, String status, int pageNo, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return userRepository.searchAndFilterUsers(keyword, status, pageable);
    }
}
