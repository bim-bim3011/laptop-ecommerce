package com.se1906.laptopshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class RegisterRequest {

     @NotBlank(message = "Full name is required")
     String fullName;

     @NotBlank(message = "Email is required")
     @Email(message = "Invalid email format")
     String email;

     @NotBlank(message = "Phone number is required")
     @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
     String phoneNumber;

     @NotBlank(message = "Address is required")
     String address;

     @NotBlank(message = "Password is required")
     @Size(min = 8, message = "Password must be at least 8 characters")
     String password;

     @NotBlank(message = "Confirm password is required")
     String confirmPassword;

}
