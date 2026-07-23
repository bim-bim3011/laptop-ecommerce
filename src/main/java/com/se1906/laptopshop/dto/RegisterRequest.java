package com.se1906.laptopshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class RegisterRequest {

     @NotBlank(message = "Họ tên không được để trống")
     @Size(min = 2, max = 50, message = "Họ tên phải từ 2 đến 50 ký tự")
     String fullName;

     @NotBlank(message = "Email không được để trống")
     @Email(message = "Email không đúng định dạng")
     String email;

     @NotBlank(message = "Số điện thoại không được để trống")
     @Pattern(regexp = "^(0|\\+84)[3|5|7|8|9][0-9]{8}$", message = "Số điện thoại không hợp lệ")
     String phoneNumber;

     @NotBlank(message = "Địa chỉ không được để trống")
     @Size(min = 5, max = 200, message = "Địa chỉ phải từ 5 đến 200 ký tự")
     String address;

     @NotBlank(message = "Mật khẩu không được để trống")
     @Size(min = 8, message = "Mật khẩu phải chứa ít nhất 8 ký tự")
     @Pattern(regexp = "^[A-Z](?=.*[^a-zA-Z0-9\\s]).{7,}$", message = "Mật khẩu phải từ 8 ký tự, chữ cái đầu viết hoa và bao gồm ít nhất 1 ký tự đặc biệt")
     String password;

     @NotBlank(message = "Xác nhận mật khẩu không được để trống")
     String confirmPassword;

}
