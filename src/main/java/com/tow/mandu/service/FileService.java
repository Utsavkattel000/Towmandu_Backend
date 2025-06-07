package com.tow.mandu.service;

import com.tow.mandu.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String saveFileToFolder(MultipartFile file) throws IOException, IOException;

    File saveFileToDatabase(MultipartFile file) throws IOException;
}
