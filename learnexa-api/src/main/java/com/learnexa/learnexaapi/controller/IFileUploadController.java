package com.learnexa.learnexaapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileUploadController {
    public ResponseEntity<?> uploadFile(MultipartFile file) throws IOException;
    public ResponseEntity<?> uploadFileBytes(byte[] byteFile);


}
