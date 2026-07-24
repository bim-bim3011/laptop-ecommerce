package com.se1906.laptopshop.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.se1906.laptopshop.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    Cloudinary cloudinary;

    @Override
    public String upload(MultipartFile file) throws IOException {
        Map<String, Object> params = ObjectUtils.asMap(
                "folder", "laptop-images",
                "resource_type", "image"
        );
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
        return uploadResult.get("secure_url").toString();
    }
}
