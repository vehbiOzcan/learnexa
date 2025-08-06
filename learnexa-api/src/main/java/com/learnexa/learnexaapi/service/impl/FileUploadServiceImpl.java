package com.learnexa.learnexaapi.service.impl;

import com.learnexa.learnexaapi.dto.DtoUploadedFile;
import com.learnexa.learnexaapi.entity.UploadedFile;
import com.learnexa.learnexaapi.exception.BaseException;
import com.learnexa.learnexaapi.exception.ErrorMessage;
import com.learnexa.learnexaapi.exception.MessageType;
import com.learnexa.learnexaapi.jwt.utils.JwtUtils;
import com.learnexa.learnexaapi.repository.UploadedFileRepository;
import com.learnexa.learnexaapi.service.IFileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class FileUploadServiceImpl implements IFileUploadService {

    @Value("${file.upload_dir}")
    private String uploadDir;

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    @Async(value = "FileUploadTaskExecutor")
    @Override
    public DtoUploadedFile uploadFile(byte[] fileContent, String contentType) {
        if (fileContent == null || fileContent.length == 0) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_EMPTY));
        }

        if (!contentType.equalsIgnoreCase("application/pdf")) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_TYPE_NOT_SUPPORTED));
        }

        String uploadedFileName = "file_" + UUID.randomUUID().toString() +"_belge"+ ".pdf";

        // Directory oluşturma
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs(); // Alt klasörleri de oluşturur
        }

        String filePath = uploadDir + uploadedFileName;
        /*Path path = Paths.get(filePath);

        try {
            if (!path.toAbsolutePath().startsWith(Paths.get(uploadDir).toAbsolutePath())) {
                throw new BaseException(new ErrorMessage(MessageType.INVALID_FILE_PATH));
            }

            Files.write(path, fileContent, StandardOpenOption.CREATE_NEW);

        } catch (IOException e) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_UPLOAD_FAILED));
        }*/

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileContent);  // Byte array'i dosyaya yazıyoruz
        } catch (IOException e) {
            throw new RuntimeException("File saving error", e);
        }

        // DB kaydı
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName(uploadedFileName);
        uploadedFile.setFileType(contentType);
        uploadedFile.setFilePath(filePath);
        uploadedFile.setFileSize((long) fileContent.length);

        UploadedFile savedFile = uploadedFileRepository.save(uploadedFile);

        return DtoUploadedFile.builder()
                .id(savedFile.getId())
                .fileName(savedFile.getFileName())
                .fileType(savedFile.getFileType())
                .filePath(savedFile.getFilePath())
                .fileSize(savedFile.getFileSize())
                .fileSizeKb((savedFile.getFileSize() / 1024.0))
                .uploadDate(savedFile.getUploadDate())
                .build();
    }


    @Async(value = "FileUploadTaskExecutor")
    @Override
    public DtoUploadedFile uploadFileBytes(byte[] fileBytes) {
        if (fileBytes == null || fileBytes.length == 0) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_EMPTY));
        }

        /*if (!isPdf(fileBytes)) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_TYPE_NOT_SUPPORTED));
        }*/


       // Yeni dosya adını oluşturuyoruz
        String uploadedFileName = /*JwtUtils.getUserNameFromSecurityContextHolder() + */"byteFile_" + UUID.randomUUID().toString() + "_" + "belge" +".pdf";

        // Directory yi oluşturuyoruz
        File dir = new File(uploadDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = uploadDir + uploadedFileName;

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileBytes);  // Byte array'i dosyaya yazıyoruz
        } catch (IOException e) {
            throw new RuntimeException("File saving error", e);
        }

       /* String filePath = uploadDir + uploadedFileName;
        Path path = Paths.get(filePath);

        try {
            if (!path.toAbsolutePath().startsWith(Paths.get(uploadDir).toAbsolutePath())) {
                throw new BaseException(new ErrorMessage(MessageType.INVALID_FILE_PATH));
            }

            Files.write(path, fileBytes, StandardOpenOption.CREATE_NEW);

        } catch (IOException e) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_UPLOAD_FAILED));
        }*/

        // Dosya bilgilerini kaydediyoruz
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName(uploadedFileName);
        uploadedFile.setFileType("application/pdf");
        uploadedFile.setFilePath(filePath);
        uploadedFile.setFileSize((long) fileBytes.length);

        UploadedFile savedFile = uploadedFileRepository.save(uploadedFile);

        return DtoUploadedFile.builder()
                .id(savedFile.getId())
                .fileName(savedFile.getFileName())
                .fileType(savedFile.getFileType())
                .filePath(savedFile.getFilePath())
                .fileSize(savedFile.getFileSize())
                .fileSizeKb((savedFile.getFileSize() / 1024.0))
                .uploadDate(savedFile.getUploadDate())
                .build();

    }

}
