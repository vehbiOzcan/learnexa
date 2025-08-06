package com.learnexa.learnexaapi.service;

import com.learnexa.learnexaapi.dto.DtoDownloadFile;

public interface IFileDownloadService {

    public DtoDownloadFile downloadFile(String fileName);
    public DtoDownloadFile downloadFileByte(String fileName);


}
