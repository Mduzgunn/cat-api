package com.md.cat.service;

import com.md.cat.repository.CatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CatServiceTest {

    @Value("${variable.cat-external-client-url}")
    private String catExternalClientUrl;

    @Value("${baseDownloadPath}")
    private String baseDownloadPath;

    @Autowired
    private CatService catService;

    @Autowired
    private CatRepository catRepository;

    @Test
    public void shouldBuildValidCustomSizeImageUrl() {
        // Given
        int width = 100;
        int height = 200;
        String expectedUrl = "https://cataas.com/cat?width=100&height=200";
        // When
        String imageUrl = catService.buildCustomSizeImageUrl(width, height);

        // Then
        assertEquals(expectedUrl, imageUrl);
    }

    @Test
    public void shouldBuildValidTaggedImageUrl() {
        // Given
        String tag = "kedi";

        // When
        String imageUrl = catService.buildTaggedImageUrl(tag);

        // Then
        assertEquals("https://cataas.com/cat?tag=kedi", imageUrl);
    }

    @Test
    public void shouldBuildValidTextualImageUrl() {
        // Given
        String text = "miyav";

        // When
        String imageUrl = catService.buildTextualImageUrl(text);

        // Then
        assertEquals("https://cataas.com/cat/says/miyav", imageUrl);
    }

    @Test
    public void shouldGenerateUniqueFileNameWhenUserSpecifiedFileNameIsNull() {
        // Given
        String userSpecifiedFileName = null;
        String directory = "your_directory_path";

        // When
        String fileName = catService.getFileName(userSpecifiedFileName, directory);

        // Then
        assertTrue(fileName.endsWith(".jpg"));
        assertFalse(Files.exists(Paths.get(directory, fileName)));
    }

    @Test
    public void shouldGenerateValidFileNameWhenUserSpecifiedFileNameIsNotNull() {
        // Given
        String userSpecifiedFileName = "kedi-resmi";
        String directory = "your_directory_path";

        // When
        String fileName = catService.getFileName(userSpecifiedFileName, directory);

        // Then
        assertEquals(userSpecifiedFileName + ".jpg", fileName);
        assertFalse(Files.exists(Paths.get(directory, fileName)));
    }

    @Test
    public void shouldSaveImageToFile() throws IOException {
        // Given
        byte[] imageBytes = {1, 2, 3, 4, 5};
        String fileName = "test.jpg";
        String directory = "C:\\\\Users\\\\Mduzgun\\\\Desktop\\\\Projects\\\\images";

        Path expectedFilePath = Paths.get(directory, fileName);

        // When
        Path filePath = catService.saveImageToFile(imageBytes, fileName, directory);

        // Then
        assertEquals(expectedFilePath, filePath);
        assertTrue(Files.exists(filePath));
        assertArrayEquals(imageBytes, Files.readAllBytes(filePath));
    }

    @Test
    public void shouldSaveImageToDefaultDirectoryIfDirectoryNotSpecified() throws IOException {
        // Given
        byte[] imageBytes = {1, 2, 3, 4, 5};
        String fileName = "test.jpg";

        Path expectedFilePath = Paths.get(baseDownloadPath, fileName);  // baseDownloadPath değerini ayarlayın

        // When
        Path filePath = catService.saveImageToFile(imageBytes, fileName, null);

        // Then
        assertEquals(expectedFilePath, filePath);
        assertTrue(Files.exists(filePath));
        assertArrayEquals(imageBytes, Files.readAllBytes(filePath));
    }

    @Test
    public void shouldThrowIOExceptionWhenDirectoryDoesNotExist() throws IOException {
        // Given
        byte[] imageBytes = {1, 2, 3, 4, 5};
        String fileName = "test.jpg";
        String directory = "invalid-directory";

        // When, Then
        assertThrows(IOException.class, () -> catService.saveImageToFile(imageBytes, fileName, directory));
    }

}

