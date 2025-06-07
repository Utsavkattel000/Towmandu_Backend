package com.tow.mandu.service.impl;

import com.tow.mandu.repository.FileRepository;
import com.tow.mandu.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.tow.mandu.enums.FileType.BUSINESS_CERTIFICATE;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public String saveFileToFolder(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String projectRoot = System.getProperty("user.dir");
        String folderPath = projectRoot + "/uploads/files/";
        File directory = new File(folderPath);

        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new IOException("Failed to create directory: " + folderPath);
            }
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String fullPath = folderPath + fileName;
        File destinationFile = new File(fullPath);

        // Copy file using InputStream to avoid temporary file issues
        try (InputStream inputStream = file.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return "/uploads/files/" + fileName;
    }

    private File createDirectoryIfNotExists(String folderPath) throws IOException {
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    @Override
    public com.tow.mandu.model.File saveFileToDatabase(MultipartFile businessCertificate) throws IOException {
        com.tow.mandu.model.File file = new com.tow.mandu.model.File();
        file.setType(BUSINESS_CERTIFICATE.name());
        file.setPath(saveFileToFolder(businessCertificate));
        fileRepository.save(file);
        return file;
    }
}
