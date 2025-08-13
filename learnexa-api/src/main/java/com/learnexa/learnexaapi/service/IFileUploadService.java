package com.learnexa.learnexaapi.service;

import com.learnexa.learnexaapi.dto.UploadedFileDto;

public interface IFileUploadService {

    public UploadedFileDto uploadFile(byte[] fileContent, String contentType);

    public UploadedFileDto uploadFileBytes(byte[] fileBytes);
}
