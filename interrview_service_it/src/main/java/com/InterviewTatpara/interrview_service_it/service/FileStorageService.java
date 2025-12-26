package com.InterviewTatpara.interrview_service_it.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    // 1. The Shared Directory (Must match what we discussed for Mac)
    // We use a fixed absolute path so the Analysis Service can find it too.
    private final String UPLOAD_DIR = "/Users/rahulkumar/interview_videos/";

    public String saveFile(MultipartFile file, UUID sessionId) throws IOException {
        // STEP 1: Ensure the directory exists
        // If the folder doesn't exist, this creates it automatically.
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // STEP 2: Create a unique filename
        // We use the Session ID (UUID) so filenames never clash.
        // We keep the original extension (e.g., .mp4)
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        } else {
            extension = ".mp4"; // Default fallback
        }

        String newFilename = sessionId.toString() + extension;

        // STEP 3: Create the full path object
        // Result: /Users/rahulkumar/interview_videos/550e8400-e29b....mp4
        Path filePath = uploadPath.resolve(newFilename);

        // STEP 4: Write the bytes to disk
        // StandardCopyOption.REPLACE_EXISTING ensures we overwrite if a retry happens.
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the full string path so we can save it to the DB
        return filePath.toString();
    }
}
