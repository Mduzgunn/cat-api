package com.md.cat.controller;

import com.md.cat.service.CatCleanUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clean")
public class CatCleanUpController {

    private final CatCleanUpService catCleanUpService;

    public CatCleanUpController(CatCleanUpService catCleanUpService) {
        this.catCleanUpService = catCleanUpService;
    }


    @PostMapping("/perform")
    public ResponseEntity<String> performCleanUp() {
        catCleanUpService.performCleanUp();
        return ResponseEntity.ok("Clean up process initiated successfully.");
    }
}
