package com.ecommerce.sb_ecom.Service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadImage(String path, MultipartFile image);
}
