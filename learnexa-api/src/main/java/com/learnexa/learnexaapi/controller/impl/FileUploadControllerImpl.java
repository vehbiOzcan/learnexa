package com.learnexa.learnexaapi.controller.impl;

import com.learnexa.learnexaapi.controller.IFileUploadController;
import com.learnexa.learnexaapi.service.IFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/uploads")
public class FileUploadControllerImpl implements IFileUploadController {

    @Autowired
    private IFileUploadService fileUploadService;

    @PostMapping("file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("File Name: " + file.getOriginalFilename());
        byte[] fileContent = file.getBytes();
        String contentType = file.getContentType();
        fileUploadService.uploadFile(fileContent, contentType);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PostMapping("file-bytes")
    @Override
    public ResponseEntity<?> uploadFileBytes(@RequestBody byte[] byteFile) {
        System.out.println("Byte Dosyası");
        fileUploadService.uploadFileBytes(byteFile);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}