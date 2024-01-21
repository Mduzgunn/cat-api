package com.md.cat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class CatCleanUpService {

    @Value("${baseDownloadPath}")
    private String baseDownloadPath;

    // Her gün saat 2:00'de çalışacak şekilde ayarlanmış zamanlayıcı
    @Scheduled(cron = "0 0 2 * * ?", zone = "GMT+3:00")
    public void performCleanUp() {
        try {
            // Temizleme işlemini gerçekleştir
            cleanUpDownloadFolder();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cleanUpDownloadFolder() throws IOException {
        Path downloadPath = Paths.get(baseDownloadPath);

        // Silinecek dosyaların sınırı: 7 gün öncesinden öncekileri silme
        //LocalDateTime boundaryDateTime = LocalDateTime.now().minusDays(7);

        // Silinecek dosyaların sınırı: 10 saniye öncesinden öncekileri silme
        LocalDateTime boundaryDateTime = LocalDateTime.now().minusSeconds(10);


        // Dosyaları gezerek belirlenen sınırdan öncekileri ve JPG uzantılı olanları silme işlemi
        Files.walk(downloadPath)
                .filter(Files::isRegularFile)
                .filter(path -> {
                    try {
                        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
                        LocalDateTime creationTime = attributes.creationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                        String fileName = path.getFileName().toString();
                        boolean isJpgFile = fileName.toLowerCase().endsWith(".jpg");

                        return creationTime.isBefore(boundaryDateTime) && isJpgFile;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

}
