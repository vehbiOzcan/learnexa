package com.learnexa.learnexaapi.service.impl;

import com.learnexa.learnexaapi.dto.DownloadFileDto;
import com.learnexa.learnexaapi.entity.UploadedFile;
import com.learnexa.learnexaapi.exception.BaseException;
import com.learnexa.learnexaapi.exception.ErrorMessage;
import com.learnexa.learnexaapi.exception.MessageType;
import com.learnexa.learnexaapi.repository.UploadedFileRepository;
import com.learnexa.learnexaapi.service.IFileDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FileDownloadServiceImpl implements IFileDownloadService {

    @Value("${file.upload_dir}")
    private String fileUploadDir;

    private Path uploadDir;

    @PostConstruct
    public void init() {
        this.uploadDir = Paths.get(fileUploadDir).toAbsolutePath().normalize();
    }

    @Autowired
    private UploadedFileRepository uploadedFileRepository;


    @Override
    public DownloadFileDto downloadFileByte(String fileName) {

        Optional<UploadedFile> optionalUploadedFile = uploadedFileRepository.findByFileName(fileName);

        if(!optionalUploadedFile.isPresent()) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_NOT_FOUND));
        }

        UploadedFile uploadedFile = optionalUploadedFile.get();

        String filePath = uploadDir.resolve(uploadedFile.getFileName()).toString();

        byte[] file = null;

        try {
           file = Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return DownloadFileDto.builder().fileData(file)
                .fileType(uploadedFile.getFileType())
                .fileName(uploadedFile.getFileName())
                .filePath(filePath)
                .build();

    }
}
