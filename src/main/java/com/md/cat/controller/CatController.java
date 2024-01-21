package com.md.cat.controller;

import com.md.cat.dto.CatDto;
import com.md.cat.service.CatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping()
@CrossOrigin(origins = "*")
public class CatController {
    private final CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<CatDto>> getPosts() {
        List<CatDto> postDtoList = catService.getAllCatDtoList();
        return ResponseEntity.ok(postDtoList);
    }

    @GetMapping("/custom-size")
    public ResponseEntity<byte[]> getCustomSizedCatImage(@RequestParam int width, @RequestParam int height,
                                                         @RequestParam(required = false) String fileName,
                                                         @RequestParam(required = false) String directory) {
        try {
            byte[] imageBytes = catService.getCustomSizedCatImage(width, height, fileName, directory);
            return ResponseEntity.ok().body(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
        }
    }

    @GetMapping("/tagged")
    public ResponseEntity<byte[]> getTaggedCatImage(@RequestParam String tag,
                                                    @RequestParam(required = false) String fileName,
                                                    @RequestParam(required = false) String directory) {
        try {
            byte[] imageBytes = catService.getTaggedCatImage(tag, fileName, directory);
            return ResponseEntity.ok().body(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
        }
    }

    @GetMapping("/textual")
    public ResponseEntity<byte[]> getTextualCatImage(@RequestParam String text,
                                                     @RequestParam(required = false) String fileName,
                                                     @RequestParam(required = false) String directory) {
        try {
            byte[] imageBytes = catService.getTextualCatImage(text, fileName, directory);
            return ResponseEntity.ok().body(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
        }
    }
}
