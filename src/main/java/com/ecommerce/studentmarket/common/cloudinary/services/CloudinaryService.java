package com.ecommerce.studentmarket.common.cloudinary.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public Map<String, Object> uploadImage(MultipartFile file) throws IOException{
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
    }

    public Map<String, Object> deleteImage(String publicId)  throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
