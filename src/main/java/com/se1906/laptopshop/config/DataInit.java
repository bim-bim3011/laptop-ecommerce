package com.se1906.laptopshop.config;

import com.se1906.laptopshop.entity.Role;
import com.se1906.laptopshop.entity.User;
import com.se1906.laptopshop.repository.RoleRepository;
import com.se1906.laptopshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .name("ADMIN").
                                    build()));

            User admin = new User();
            admin.setFullName("System Admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setPhoneNumber("0000000000");
            admin.setStatus("ACTIVE");
            admin.setRoles(new HashSet<>(Set.of(adminRole)));

            userRepository.save(admin);
            System.out.println("Admin user 'admin@gmail.com' created successfully.");
        }
    }
}
