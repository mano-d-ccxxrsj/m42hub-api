package com.m42hub.m42hub_api.file.controller;

import com.m42hub.m42hub_api.file.service.ImgBBService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/image")
public class ImageController {
    private final ImgBBService imgBBService;

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('image:upload')")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String url = imgBBService.uploadImage(file);
        return ResponseEntity.ok(url);
    }
}
