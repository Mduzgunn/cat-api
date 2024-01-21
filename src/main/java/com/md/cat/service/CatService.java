package com.md.cat.service;

import com.md.cat.entity.Cat;
import com.md.cat.enums.ContentFormat;
import com.md.cat.repository.CatRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class CatService {
    @Value("${baseDownloadPath}")
    private String baseDownloadPath;

    @Value("${variable.cat-external-client-url}")
    private String catExternalClientUrl;
    private final CatRepository catRepository;

    public CatService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    @PostConstruct
    private void initializeDownloadPath() throws IOException {
        // Eğer application.properties -> baseDownloadPath değeri null veya boşsa, JVM seçeneğini kontrol et
        if (baseDownloadPath == null || baseDownloadPath.isBlank()) {
            // JVM seçeneğini kontrol et
            String customDownloadPath = System.getProperty("baseDownloadPathJVM");

            // Eğer JVM seçeneği belirlenmişse ve geçerli bir dizinse kullan
            if (customDownloadPath != null && Files.isDirectory(Paths.get(customDownloadPath))) {
                baseDownloadPath = customDownloadPath;
            } else {
                // Eğer belirtilmemişse veya geçerli bir dizin değilse varsayılan bir değer kullan
                baseDownloadPath = "C:\\Temp";
            }
        }

        // Dizin yoksa oluştur
        Path downloadPath = Paths.get(baseDownloadPath);
        if (Files.notExists(downloadPath)) {
            Files.createDirectories(downloadPath);
        }
    }

    /**
     * Belirli boyutlarda özel bir kedi resminin URL'sini oluşturan metod.
     * @param width Resim genişliği.
     * @param height Resim yüksekliği.
     * @return Oluşturulan URL.
     */
    protected String buildCustomSizeImageUrl(int width, int height) {
        return UriComponentsBuilder.fromHttpUrl(catExternalClientUrl)
                .queryParam("width", width)
                .queryParam("height", height)
                .build()
                .toUriString();
    }

    /**
     * Belirli bir etiketle işaretlenmiş bir kedi resminin URL'sini oluşturan metod.
     * @param tag Kullanılacak etiket.
     * @return Oluşturulan URL.
     */
    protected String buildTaggedImageUrl(String tag) {
        return UriComponentsBuilder.fromHttpUrl(catExternalClientUrl)
                .queryParam("tag", tag)
                .build()
                .toUriString();
    }

    /**
     * Belirli bir metni içeren bir kedi resminin URL'sini oluşturan metod.
     * @param text Kullanılacak metin.
     * @return Oluşturulan URL.
     */
    protected String buildTextualImageUrl(String text) {
        return UriComponentsBuilder.fromHttpUrl(catExternalClientUrl + "/says/")
                .path(text)
                .build()
                .toUriString();
    }

    /**
     * Benzersiz bir dosya adı oluşturan metod.
     * @return Oluşturulan dosya adı.
     */
    private String generateUniqueFileName() {
        AtomicInteger counter = new AtomicInteger(0);
        return Stream.iterate("cat_image.jpg", name -> "cat_image(" + counter.incrementAndGet() + ").jpg")
                .filter(name -> !Files.exists(Paths.get(baseDownloadPath, name)))
                .findFirst()
                .orElseThrow(); // Uygun bir dosya adı bulunamazsa isteğe bağlı olarak hata fırlatılabilir.
    }

    /**
     * Belirtilen byte dizisini dosyaya kaydeden metod.
     * @param imageBytes Kaydedilecek byte dizisi.
     * @param fileName Kullanıcının isteğine bağlı dosya adı.
     * @param directory Kullanıcının isteğine bağlı olarak belirtilen dizin.
     * @return Kaydedilen dosyanın yolu içeren Path nesnesi.
     * @throws IOException Kaydetme hatası durumunda fırlatılır.
     */
    protected Path saveImageToFile(byte[] imageBytes, String fileName, String directory) throws IOException {
        String basePath = directory != null ? directory : baseDownloadPath;
        Path filePath = Paths.get(basePath, fileName);
        Files.write(filePath, imageBytes);
        return filePath;
    }


    /**
     * Kedi bilgilerini veritabanına kaydeden metod.
     * @param fileName Kedi resminin dosya adı.
     * @param contentFormat Kedi resminin içerik formatı.
     * @param filePath Kedi resminin dosya yolu.
     * @param catValue Kedi resminin değeri (boyut, etiket veya metin).
     */
    protected void saveCatToRepository(String fileName, ContentFormat contentFormat, String filePath, String catValue) {
        Cat cat = new Cat();
        //cat.setId(UUID.randomUUID().toString());
        cat.setName(fileName);
        cat.setValue(catValue);
        cat.setContentFormat(contentFormat);
        cat.setPath(filePath);
        catRepository.save(cat);
    }

    /**
     * Belirli boyutlarda özel bir kedi resminin URL'sini oluşturan metod.
     * @param width Resim genişliği.
     * @param height Resim yüksekliği.
     * @param fileName Kullanıcının isteğine bağlı dosya adı.
     * @param directory Kullanıcının isteğine bağlı olarak belirtilen dizin.
     * @return Oluşturulan URL.
     * @throws IOException URL oluşturma veya resmi indirme hatası durumunda fırlatılır.
     */
    public byte[] getCustomSizedCatImage(int width, int height, String fileName, String directory) throws IOException {
        String imageUrl = buildCustomSizeImageUrl(width, height);
        byte[] imageBytes = downloadAndRetrieve(imageUrl);
        String finalFileName = getFileName(fileName);
        Path filePath = saveImageToFile(imageBytes, finalFileName, directory);
        saveCatToRepository(finalFileName, ContentFormat.Custom, filePath.toString(), "width: " + width + " height: " + height);
        return imageBytes;
    }

    /**
     * Belirli bir etiketle işaretlenmiş bir kedi resminin URL'sini oluşturan metod.
     * @param tag Kullanılacak etiket.
     * @param fileName Kullanıcının isteğine bağlı dosya adı.
     * @param directory Kullanıcının isteğine bağlı olarak belirtilen dizin.
     * @return Oluşturulan URL.
     * @throws IOException URL oluşturma veya resmi indirme hatası durumunda fırlatılır.
     */
    public byte[] getTaggedCatImage(String tag, String fileName, String directory) throws IOException {
        String imageUrl = buildTaggedImageUrl(tag);
        byte[] imageBytes = downloadAndRetrieve(imageUrl);
        String finalFileName = getFileName(fileName);
        Path filePath = saveImageToFile(imageBytes, finalFileName, directory);
        saveCatToRepository(finalFileName, ContentFormat.Tag, filePath.toString(), "tag: " + tag);
        return imageBytes;
    }

    /**
     * Belirli bir metni içeren bir kedi resminin URL'sini oluşturan metod.
     * @param text Kullanılacak metin.
     * @param fileName Kullanıcının isteğine bağlı dosya adı.
     * @param directory Kullanıcının isteğine bağlı olarak belirtilen dizin.
     * @return Oluşturulan URL.
     * @throws IOException URL oluşturma veya resmi indirme hatası durumunda fırlatılır.
     */
    public byte[] getTextualCatImage(String text, String fileName, String directory) throws IOException {
        String imageUrl = buildTextualImageUrl(text);
        byte[] imageBytes = downloadAndRetrieve(imageUrl);
        String finalFileName = getFileName(fileName);
        Path filePath = saveImageToFile(imageBytes, finalFileName, directory);
        saveCatToRepository(finalFileName, ContentFormat.Text, filePath.toString(), "text: " + text);
        return imageBytes;
    }

    /**
     * Kullanıcı tarafından belirtilen dosya adını uygun bir formata getirir.
     * Eğer kullanıcı tarafından belirtilen dosya adı yoksa, otomatik bir ad oluşturur.
     *
     * @param userSpecifiedFileName Kullanıcı tarafından belirtilen dosya adı
     * @return Uygun bir dosya adı
     */
    protected String getFileName(String userSpecifiedFileName) {
        if (userSpecifiedFileName != null && !userSpecifiedFileName.isBlank()) {
            return userSpecifiedFileName.endsWith(".jpg") ? userSpecifiedFileName : userSpecifiedFileName + ".jpg";
        } else {
            return generateUniqueFileName();
        }
    }

    /**
     * Belirtilen URL'den byte dizisi indiren ve döndüren metod.
     * @param imageUrl İndirilecek resmin URL'si.
     * @return İndirilen resmin byte dizisi.
     * @throws IOException İndirme hatası durumunda fırlatılır.
     */
    protected byte[] downloadAndRetrieve(String imageUrl) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return Optional.ofNullable(restTemplate.getForObject(imageUrl, byte[].class))
                    .orElseThrow(() -> new IOException("Failed to download image from API."));
        } catch (Exception e) {
            throw new IOException("Failed to download image from API. Error: " + e.getMessage());
        }
    }
}
