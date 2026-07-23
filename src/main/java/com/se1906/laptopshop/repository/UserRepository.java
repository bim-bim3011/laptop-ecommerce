package com.se1906.laptopshop.repository;

import com.se1906.laptopshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE' AND (u.isDeleted IS NULL OR u.isDeleted = false)")
    long countActiveUsers();

    @org.springframework.data.jpa.repository.Query("SELECT u FROM User u WHERE (u.isDeleted IS NULL OR u.isDeleted = false) AND " +
           "(:keyword IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:status IS NULL OR :status = '' OR u.status = :status)")
    Page<User> searchAndFilterUsers(@Param("keyword") String keyword, @Param("status") String status, Pageable pageable);
    
    @org.springframework.data.jpa.repository.Query("SELECT u FROM User u WHERE u.isDeleted IS NULL OR u.isDeleted = false")
    java.util.List<User> findActiveUsers();
}
