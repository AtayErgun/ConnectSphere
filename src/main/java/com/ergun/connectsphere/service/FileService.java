package com.ergun.connectsphere.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {

    private static final String UPLOAD_DIR = "uploads/";

    public String saveFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String extension = Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename().lastIndexOf("."));

            String fileName = UUID.randomUUID() + extension;

            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), filePath);

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
}
