package com.learnexa.learnexaapi.controller.impl;

import com.learnexa.learnexaapi.controller.IFileDownloadController;
import com.learnexa.learnexaapi.dto.DtoDownloadFile;
import com.learnexa.learnexaapi.service.IFileDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/files")
public class FileDownloadControllerImpl implements IFileDownloadController {

    @Autowired
    private IFileDownloadService fileDownloadService;

    @GetMapping("view/{fileName}")
    @Override
    public ResponseEntity<?> downloadFileByte(@PathVariable("fileName") String fileName) {
        DtoDownloadFile file = fileDownloadService.downloadFileByte(fileName);
        return ResponseEntity.ok().contentType(MediaType.valueOf(file.getFileType())).body(file.getFileData()) ;
    }
}
