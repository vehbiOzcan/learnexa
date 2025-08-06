package com.learnexa.learnexaapi.service;

import com.learnexa.learnexaapi.dto.DtoUploadedFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface IFileUploadService {

    public DtoUploadedFile uploadFile(byte[] fileContent, String contentType);
    public DtoUploadedFile uploadFileTest(byte[] fileContent, long startEpoch);
    public DtoUploadedFile uploadMockFile(MultipartFile file);
    public DtoUploadedFile uploadMockFileTest(MultipartFile file, long startEpoch);
    public DtoUploadedFile uploadFileBytes(byte[] fileBytes);
    public DtoUploadedFile uploadFileBytesTest(byte[] fileBytes, long startEpoch);
    public CompletableFuture<DtoUploadedFile> uploadFileBytesFuture(byte[] fileBytes);
    public void processFile(byte[] fileData);
    public CompletableFuture<String> processFile2(byte[] fileData);
    public double calculateAverageEpochTime(String fileName);
}
