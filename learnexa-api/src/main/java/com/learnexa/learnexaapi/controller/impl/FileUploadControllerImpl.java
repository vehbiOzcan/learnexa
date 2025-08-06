package com.learnexa.learnexaapi.controller.impl;

import com.learnexa.learnexaapi.controller.IFileUploadController;
import com.learnexa.learnexaapi.dto.DtoUploadedFile;
import com.learnexa.learnexaapi.entity.RootEntity;
import com.learnexa.learnexaapi.service.IFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/uploads")
public class FileUploadControllerImpl implements IFileUploadController {

    @Autowired
    private IFileUploadService fileUploadService;

    @PostMapping("file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("File Name: "+ file.getOriginalFilename());
        byte[] fileContent = file.getBytes();
        String contentType = file.getContentType();
        fileUploadService.uploadFile(fileContent, contentType);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("file-test/{startEpoch}")
    @Override
    public ResponseEntity<?> uploadFileTest(@RequestParam("file") MultipartFile file, @PathVariable long startEpoch) throws IOException {

        byte[] fileContent = file.getBytes();
        String contentType = file.getContentType();

        // Döngü içinde aynı dosyayı kopyalayarak işlem yapalım
        for (int i = 0; i < 1000; i++) {

            fileUploadService.uploadFile(fileContent, contentType);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("file-bytes")
    @Override
    public ResponseEntity<?> uploadFileBytes(@RequestBody byte[] byteFile) {
        System.out.println("Byte Dosyası");
        fileUploadService.uploadFileBytes(byteFile);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("file-bytes-test/{startEpoch}")
    @Override
    public ResponseEntity<?> uploadFileBytesTest(@RequestBody byte[] byteFile, @PathVariable long startEpoch) {

        for (int i = 0; i < 1000; i++) {
            fileUploadService.uploadFileBytesTest(byteFile, startEpoch);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("file-bytes-future")
    @Override
    public RootEntity<DtoUploadedFile> uploadFileBytesFuture(@RequestBody byte[] byteFile) {
        System.out.println("Girdi burada şuan");
        CompletableFuture<DtoUploadedFile> future = fileUploadService.uploadFileBytesFuture(byteFile);

        return future.thenApply(dto -> RootEntity.ok(dto)).join();
    }


    @Override
    @GetMapping("epoch-avg/{fileName}")
    public String avgEpoch(@PathVariable String fileName) {
        double averageEpochTime = 0;
        try {
            averageEpochTime = fileUploadService.calculateAverageEpochTime(fileName);
            System.out.println("Ortalama Epoch Süresi: " + averageEpochTime + " ms");
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
        return String.valueOf(averageEpochTime);  // join() ile işlemin tamamlanmasını bekleyip sonucu döndürüyoruz
    }

}
