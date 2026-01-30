package com.ergun.connectsphere.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
@Service
public class FileService {

    private final Cloudinary cloudinary;

    // Bilgileri application.yml'den çekip Cloudinary bağlantısını kuruyoruz
    public FileService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {

        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    // Bu metod hem resim yükler hem de bize internet URL'ini döner
    public String saveFile(MultipartFile file, String subDir) {
        if (file.isEmpty()) {
            throw new RuntimeException("Dosya boş!");
        }

        try {
            // Dosyayı Cloudinary'ye gönderiyoruz
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "connectsphere/" + subDir));

            // Yüklenen resmin internet adresini (URL) döndürüyoruz
            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Cloudinary yükleme hatası!", e);
        }
    }

    // Eski metodların bozulmaması için (default klasör: general)
    public String saveFile(MultipartFile file) {
        return saveFile(file, "general");
    }
}