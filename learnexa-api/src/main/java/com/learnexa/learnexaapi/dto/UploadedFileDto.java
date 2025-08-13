package com.learnexa.learnexaapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadedFileDto {

    private Long id;

    private String fileName;
    private String fileType;

    private String filePath;
    private Long fileSize;
    private Double fileSizeKb;
    private Date uploadDate;
    private Long processTime;

}
