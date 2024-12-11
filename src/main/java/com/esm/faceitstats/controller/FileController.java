package com.esm.faceitstats.controller;


import com.esm.faceitstats.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.InputStream;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private StorageService storageService;

    @Autowired
    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/path")
    public ResponseEntity<?> getFileContentStream(@RequestParam String filename) {
        var parts = filename.split("/");
        filename = parts[parts.length - 1];
        InputStream inputStream = this.storageService.getFile(filename);
        if (inputStream == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment");
        headers.add("filename", '"' + filename + '"');
        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(headers)
                .body(resource);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllFiles() {
        var paths = this.storageService.getAllPaths();
        return ResponseEntity.ok().body(paths);
    }
}
