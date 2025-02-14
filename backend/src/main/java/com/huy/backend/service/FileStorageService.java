package com.huy.backend.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {
    private Path fileStorageLocation;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.max-size}") // 5MB default
    private long maxFileSize;

    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create upload directory", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        validateFile(file);

        try {
            String fileName = generateUniqueFileName(file);
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file. Please try again!", ex);
        }
    }

    public void deleteFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            log.warn("Attempted to delete file with null or empty filename");
            return;
        }

        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            // Security check - verify the file is within uploads directory
            if (!filePath.getParent().equals(this.fileStorageLocation)) {
                throw new SecurityException("Cannot delete file outside upload directory");
            }

            // Check if file exists before attempting deletion
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Successfully deleted file: {}", fileName);
            } else {
                log.warn("File not found for deletion: {}", fileName);
            }
        } catch (IOException ex) {
            log.error("Error occurred while deleting file: {}", fileName, ex);
            throw new RuntimeException("Could not delete file: " + fileName, ex);
        } catch (SecurityException ex) {
            log.error("Security violation while attempting to delete file: {}", fileName, ex);
            throw new RuntimeException("Security violation: " + ex.getMessage());
        }
    }

    public boolean fileExists(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            return Files.exists(filePath);
        } catch (Exception ex) {
            return false;
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        // Check file size
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException(
                    String.format("File size exceeds maximum limit of %d bytes", maxFileSize)
            );
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // Validate file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename() != null ?
                file.getOriginalFilename() : "");
        if (fileName.contains("..")) {
            throw new IllegalArgumentException(
                    "Filename contains invalid path sequence: " + fileName
            );
        }
    }

    private String generateUniqueFileName(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename() != null ?
                file.getOriginalFilename() : "");
        String fileExtension = getFileExtension(originalFileName);

        return UUID.randomUUID().toString() + fileExtension;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}

