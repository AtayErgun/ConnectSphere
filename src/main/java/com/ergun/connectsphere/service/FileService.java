package com.ergun.connectsphere.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {

    private static final String BASE_UPLOAD_DIR = "uploads/";

    // 1. ESKİ KODUN ÇALIŞMASI İÇİN (Geriye dönük uyumluluk)
    // Mesaj resimleri direkt uploads/ içine gitmeye devam eder.
    public String saveFile(MultipartFile file) {
        return saveFile(file, ""); // Boş subDir ile çağırıyoruz
    }

    // 2. YENİ METOD: ALT KLASÖR DESTEĞİ
    // Profil fotoları için "avatars" parametresi gönderilecek.
    public String saveFile(MultipartFile file, String subDir) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        try {
            // Hedef yolu belirle: uploads/ + altKlasör (örn: avatars)
            Path uploadPath = Paths.get(BASE_UPLOAD_DIR, subDir);

            // Eğer klasör yoksa (uploads/avatars gibi) otomatik oluşturur
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Dosya uzantısını al (.jpg, .png vb.)
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // Benzersiz dosya adı oluştur
            String fileName = UUID.randomUUID().toString() + extension;

            // Dosyayı fiziksel olarak kaydet
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
}
