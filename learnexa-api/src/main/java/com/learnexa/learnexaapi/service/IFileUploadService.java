package com.learnexa.learnexaapi.service;

import com.learnexa.learnexaapi.dto.DtoUploadedFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface IFileUploadService {

    public DtoUploadedFile uploadFile(byte[] fileContent, String contentType);

    public DtoUploadedFile uploadFileBytes(byte[] fileBytes);
}
