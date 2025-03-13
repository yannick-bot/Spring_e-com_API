package com.ecommerce.sb_ecom.ServiceImplementation;

import com.ecommerce.sb_ecom.Service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile image) {
        // File names of current/original file
        String originalFileName = image.getOriginalFilename();

        // Generate a unique file name
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        // String filePath = path + File.separator + fileName;
        Path filePath = Path.of(path, fileName);

        // Check if path exists and create
        File folder = new File(path);
        if (!folder.exists()) folder.mkdir();

        // Upload to server
        try {
            image.transferTo(filePath);
        } catch (IOException e) {
            return e.getClass().getSimpleName() + ": Error uploading the file " + e.getMessage();
        }
        //returning fileName
        return fileName;
    }
}
