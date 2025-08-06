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
    public DtoUploadedFile uploadFileTest(byte[] fileContent, long startEpoch) {
        if (fileContent == null || fileContent.length == 0) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_EMPTY));
        }


        String uploadedFileName ="file_" + UUID.randomUUID().toString() +"_belge"+ ".pdf";

        // Directory oluşturma
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs(); // Alt klasörleri de oluşturur
        }

        String filePath = uploadDir + uploadedFileName;
        Path path = Paths.get(filePath);

        try {
            if (!path.toAbsolutePath().startsWith(Paths.get(uploadDir).toAbsolutePath())) {
                throw new BaseException(new ErrorMessage(MessageType.INVALID_FILE_PATH));
            }

            Files.write(path, fileContent, StandardOpenOption.CREATE_NEW);

        } catch (IOException e) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_UPLOAD_FAILED));
        }

        // DB kaydı
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName(uploadedFileName);
        uploadedFile.setFileType("application/pdf");
        uploadedFile.setFilePath(filePath);
        uploadedFile.setFileSize((long) fileContent.length);

        UploadedFile savedFile = uploadedFileRepository.save(uploadedFile);

        System.out.println("EPOCH TIME:"+(System.currentTimeMillis() - startEpoch));
        long elapsedTime = System.currentTimeMillis() - startEpoch;

        // Log dosyasına yaz
        logEpochTime(elapsedTime,"epochLogMultipart");

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

    @Async(value = "FileUploadTaskExecutor")
    @Override
    public DtoUploadedFile uploadFileBytesTest(byte[] fileBytes, long startEpoch) {
        if (fileBytes == null || fileBytes.length == 0) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_EMPTY));
        }

        if (!isPdf(fileBytes)) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_TYPE_NOT_SUPPORTED));
        }


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

        // Dosya bilgilerini kaydediyoruz
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName(uploadedFileName);
        uploadedFile.setFileType("application/pdf");
        uploadedFile.setFilePath(filePath);
        uploadedFile.setFileSize((long) fileBytes.length);

        UploadedFile savedFile = uploadedFileRepository.save(uploadedFile);

        System.out.println("EPOCH TIME:"+(System.currentTimeMillis() - startEpoch));
        long elapsedTime = System.currentTimeMillis() - startEpoch;

        // Log dosyasına yaz
        logEpochTime(elapsedTime,"epochLogBytes");

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
    public DtoUploadedFile uploadMockFile(MultipartFile file) {

        if(file.isEmpty() || file == null){
            throw new BaseException(new ErrorMessage(MessageType.FILE_EMPTY));
        }

        // Dosyanın content type nı aldık
        String contentType = file.getContentType();

        if (!contentType.contains("pdf")) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_TYPE_NOT_SUPPORTED));
        }

        // Orjinal dosya adı burada
        String orgFileName = file.getOriginalFilename();
        // Yeni dosya adını SecurityContext içinden aldığımız username ile oluşturuduk
        String uploadedFileName = "mockFile_" + UUID.randomUUID().toString() + "_" + (orgFileName != null ? orgFileName : "unknown") ;

        // Directory yi oluşturduk
        File dir = new File(uploadDir);

        // directitory var mı yok mu bakıyoruz yoksa oluşturuyoruz?
        if (!dir.exists()) {
            // directory yoksa tüm alt directorylerine bakarak sırayla oluşturur
            dir.mkdirs();
        }

        String filePath = uploadDir + uploadedFileName;

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName(uploadedFileName);
        uploadedFile.setFileType(contentType);
        uploadedFile.setFilePath(filePath);
        uploadedFile.setFileSize(file.getSize());

        UploadedFile savedFile = uploadedFileRepository.save(uploadedFile);

         DtoUploadedFile savedDtoFile = DtoUploadedFile.builder()
                 .id(savedFile.getId())
                 .fileName(savedFile.getFileName())
                 .fileType(savedFile.getFileType())
                 .filePath(savedFile.getFilePath())
                 .fileSize(savedFile.getFileSize())
                 .fileSizeKb((savedFile.getFileSize() / 1024.0))
                 .uploadDate(savedFile.getUploadDate())
                 .build();

        return savedDtoFile;
    }

    @Async("FileUploadTaskExecutor")
    @Override
    public DtoUploadedFile uploadMockFileTest(MultipartFile file, long startEpoch) {

        if(file.isEmpty() || file == null){
            throw new BaseException(new ErrorMessage(MessageType.FILE_EMPTY));
        }


        // Dosyanın content type nı aldık
        String contentType = file.getContentType();

        if (!contentType.contains("pdf")) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_TYPE_NOT_SUPPORTED));
        }

        // Orginal dosya adı burada
        String orgFileName = file.getOriginalFilename();
        // Yeni dosya adını SecurityContext içinden aldığımız username ile oluşturuduk
        String uploadedFileName = /*JwtUtils.getUserNameFromSecurityContextHolder() +*/ "mockFile_" + UUID.randomUUID().toString() + "_" + (orgFileName != null ? orgFileName : "unknown") ;

        // Directory yi oluşturduk
        File dir = new File(uploadDir);

        // directitory var mı yok mu bakıyoruz yoksa oluşturuyoruz?
        if (!dir.exists()) {
            // directory yoksa tüm alt directorylerine bakarak sırayla oluşturur
            dir.mkdirs();
        }

        String filePath = uploadDir + uploadedFileName;

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName(uploadedFileName);
        uploadedFile.setFileType(contentType);
        uploadedFile.setFilePath(filePath);
        uploadedFile.setFileSize(file.getSize());

        UploadedFile savedFile = uploadedFileRepository.save(uploadedFile);

        System.out.println("EPOCH TIME:"+(System.currentTimeMillis() - startEpoch));
        long elapsedTime = System.currentTimeMillis() - startEpoch;

        // Log dosyasına yaz
        logEpochTime(elapsedTime,"epochLogMultipart");

         DtoUploadedFile savedDtoFile = DtoUploadedFile.builder()
                 .id(savedFile.getId())
                 .fileName(savedFile.getFileName())
                 .fileType(savedFile.getFileType())
                 .filePath(savedFile.getFilePath())
                 .fileSize(savedFile.getFileSize())
                 .fileSizeKb((savedFile.getFileSize() / 1024.0))
                 .uploadDate(savedFile.getUploadDate())
                 .build();

        return savedDtoFile;
    }

    @Async(value = "FileUploadTaskExecutor")
    @Override
    public CompletableFuture<DtoUploadedFile> uploadFileBytesFuture(byte[] fileBytes) {
        System.out.println(Thread.currentThread().getName());
        if (fileBytes == null || fileBytes.length == 0) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_EMPTY));
        }

        if (!isPdf(fileBytes)) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_TYPE_NOT_SUPPORTED));
        }

        // Yeni dosya adını oluşturuyoruz
        String uploadedFileName = (new Date().toString()) + "_" + UUID.randomUUID().toString() + "_byteFiles" + ".pdf";

        // Directory yi oluşturuyoruz
        File dir = new File(uploadDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = uploadDir + uploadedFileName;

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileBytes);  // Byte array'i dosyaya yazıyoruz
        } catch (IOException e) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_UPLOAD_FAILED));
        }


        // Dosya bilgilerini kaydediyoruz
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName(uploadedFileName);
        uploadedFile.setFileType("application/pdf");
        uploadedFile.setFilePath(filePath);
        uploadedFile.setFileSize((long) fileBytes.length);

        UploadedFile savedFile = uploadedFileRepository.save(uploadedFile);

        return CompletableFuture.completedFuture(DtoUploadedFile.builder()
                .id(savedFile.getId())
                .fileName(savedFile.getFileName())
                .fileType(savedFile.getFileType())
                .filePath(savedFile.getFilePath())
                .fileSize(savedFile.getFileSize())
                .fileSizeKb((savedFile.getFileSize() / 1024.0))
                .uploadDate(savedFile.getUploadDate())
                .build());
    }


/*

    @Override
    public DtoUploadedFile uploadFileBytes(byte[] fileBytes, String originalFileName, String contentType) {

        if (fileBytes == null || fileBytes.length == 0) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_EMPTY));
        }

        if (!isPdf(fileBytes)) {
            throw new BaseException(new ErrorMessage(MessageType.FILE_TYPE_NOT_SUPPORTED));
        }

        // Yeni dosya adını oluşturuyoruz
        String uploadedFileName = JwtUtils.getUserNameFromSecurityContextHolder() + "_" + UUID.randomUUID().toString() + "_" + originalFileName +".pdf";

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

        // Dosya bilgilerini kaydediyoruz
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setFileName(uploadedFileName);
        uploadedFile.setFileType(contentType);
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
*/


    //byte[] olarak gelen dosyalarda pdf kontrolu yapan metot ilk 4 karakter %PDF olarak başlar
    private boolean isPdf(byte[] fileBytes) {
        // PDF dosyalarının başında %PDF ile başlar
        return (fileBytes[0] == 0x25 && fileBytes[1] == 0x50 && fileBytes[2] == 0x44 && fileBytes[3] == 0x46);
    }

    //SecurityContextHolder üzerinden username'i alan metot.
    private String getUserNameFromSecurityContextHolder() {
        String name = JwtUtils.getUserNameFromSecurityContextHolder();
        return name;
    }


    //Threadlerin doğru çalışıp çalışmadığını test etmek için yazdım
    public void processFile(byte[] fileData) {
        try {
            // İşin yapıldığını simüle ediyoruz: 2 saniye bekleyelim
            System.out.println("Processing file...");
            Thread.sleep(3000);  //3 saniye simülasyonu
            System.out.println("File processed.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Future ile Threadlerin doğru çalışıp çalışmadığını test etmek için yazdım
    @Async(value = "FileUploadTaskExecutor")
    public CompletableFuture<String> processFile2(byte[] fileData) {
        try {
            // Dosya işleme simülasyonu (farklı süreler alabilir)
            Thread.sleep(2000);  // 2 saniye simülasyonu, dosyanın büyüklüğüne göre işlem süresi değişir

            // İş tamamlandıktan sonra başarı mesajı döndürülüyor
            return CompletableFuture.completedFuture("File processed successfully.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture("Error processing file.");
        }
    }

    //Epoch sürelerini istenilen dosyaya yazdrımak için yazdığım metot
    private void logEpochTime(long elapsedTime,String fileName) {
        String logFilePath = "D:/epochLogs/"+ fileName +".txt";
        File logFile = new File(logFilePath);

        // Eğer dosya veya dizin yoksa oluştur
        try {
            File parentDir = logFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            // Dosyaya satır ekle
            try (FileWriter fileWriter = new FileWriter(logFile, true);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write(String.valueOf(elapsedTime));
                bufferedWriter.newLine(); // Yeni satır ekler
            }
        } catch (IOException e) {
            throw new RuntimeException("Log yazma hatası", e);
        }
    }

    //Dosya olarak kayıt ettiğim epoch sürelerinin ortalamasını verir
    public double calculateAverageEpochTime(String fileName) {
        String logFilePath = "D:/epochLogs/"+fileName+".txt";
        File logFile = new File(logFilePath);

        if (!logFile.exists()) {
            throw new RuntimeException("Log dosyası bulunamadı: " + logFilePath);
        }

        long totalEpoch = 0;
        int count = 0;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(logFile))) {
            String line;

            // Her satırı oku ve toplam epoch süresini hesapla
            while ((line = bufferedReader.readLine()) != null) {
                try {
                    totalEpoch += Long.parseLong(line); // Satırı long olarak çevir ve topla
                    count++;
                } catch (NumberFormatException e) {
                    System.err.println("Geçersiz epoch değeri: " + line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Log dosyası okunurken bir hata oluştu", e);
        }

        if (count == 0) {
            throw new RuntimeException("Log dosyasında geçerli epoch verisi bulunamadı.");
        }

        // Ortalama epoch süresini hesapla
        return (double) totalEpoch / count;
    }


}
