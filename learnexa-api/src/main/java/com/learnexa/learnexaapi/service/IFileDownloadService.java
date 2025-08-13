package com.learnexa.learnexaapi.service;

import com.learnexa.learnexaapi.dto.DownloadFileDto;

public interface IFileDownloadService {

    public DownloadFileDto downloadFileByte(String fileName);


}
