package com.learnexa.learnexaapi.controller;

import com.learnexa.learnexaapi.dto.DtoUploadedFile;
import com.learnexa.learnexaapi.entity.RootEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileUploadController {
    public ResponseEntity<?> uploadFile(MultipartFile file) throws IOException;
    public ResponseEntity<?> uploadFileTest(MultipartFile file, long startEpoch) throws IOException;
    public ResponseEntity<?> uploadFileBytes(byte[] byteFile);
    public ResponseEntity<?> uploadFileBytesTest(byte[] byteFile, long startEpoch);
    public RootEntity<DtoUploadedFile> uploadFileBytesFuture(byte[] byteFile);
    public String avgEpoch(String fileName);

}
