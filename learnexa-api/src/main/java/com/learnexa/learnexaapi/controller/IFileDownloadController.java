package com.learnexa.learnexaapi.controller;

import org.springframework.http.ResponseEntity;

public interface IFileDownloadController {
    public ResponseEntity<?> downloadFileByte(String fileName);

}
