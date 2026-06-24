package com.se1906.laptopshop.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class RegisterRequest {

     String firstName;
     String lastName;
     String email;
     String password_hash;
     String phone;


}
