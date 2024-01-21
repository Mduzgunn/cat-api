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

        // When
        String fileName = catService.getFileName(userSpecifiedFileName);

        // Then
        assertTrue(fileName.endsWith(".jpg"));
        assertFalse(Files.exists(Paths.get(baseDownloadPath, fileName)));
    }

    @Test
    public void shouldGenerateValidFileNameWhenUserSpecifiedFileNameIsNotNull() {
        String userSpecifiedFileName = "kedi-resmi";

        String fileName = catService.getFileName(userSpecifiedFileName);

        assertEquals(userSpecifiedFileName + ".jpg", fileName);
        assertFalse(Files.exists(Paths.get(baseDownloadPath, fileName)));
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

 /*
    @Test
    public void shouldGetCustomSizedCatImage() throws IOException {
        // Given
        int width = 100;
        int height = 200;
        String fileName = "test.jpg";
        String directory = "/path/to/dir";
        byte[] expectedImageBytes = {};
        String expectedImageUrl = "https://example.com/cat/100x200";

        // Mock external dependencies
        Mockito.when(catService.buildCustomSizeImageUrl(width, height)).thenReturn(expectedImageUrl);
        Mockito.when(catService.downloadAndRetrieve(expectedImageUrl)).thenReturn(expectedImageBytes);
        Mockito.when(catService.getFileName(fileName)).thenReturn("final_test.jpg");
        Mockito.when(catService.saveImageToFile(expectedImageBytes, "final_test.jpg", directory))
                .thenReturn(Paths.get("/path/to/dir/final_test.jpg"));

        // When
        byte[] actualImageBytes = catService.getCustomSizedCatImage(width, height, fileName, directory);

        // Then
        assertArrayEquals(expectedImageBytes, actualImageBytes);
        Mockito.verify(catService.buildCustomSizeImageUrl(width, height));
        Mockito.verify(catService.downloadAndRetrieve(expectedImageUrl));
        Mockito.verify(catService.getFileName(fileName));
        Mockito.verify(catService.saveImageToFile(expectedImageBytes, "final_test.jpg", directory));
        Mockito.verify(catService.saveCatToRepository(
                "final_test.jpg",
                ContentFormat.Tag,
                "/path/to/dir/final_test.jpg",
                "width: 100 height: 200"));
    }
*/

}

