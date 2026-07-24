package com.se1906.laptopshop.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String upload(MultipartFile file) throws IOException;
}
