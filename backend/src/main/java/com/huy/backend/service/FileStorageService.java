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

    @Value("${file.max-size}") // e.g., 5242880 bytes for 5MB
    private long maxFileSize;

    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("Upload directory set to: {}", this.fileStorageLocation);
        } catch (IOException ex) {
            log.error("Could not create upload directory: {}", this.fileStorageLocation, ex);
            throw new RuntimeException("Could not create upload directory", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Validate file attributes (size, type, filename)
        validateFile(file);

        // Generate a unique file name and resolve the target location
        String fileName = generateUniqueFileName(file);
        Path targetLocation = this.fileStorageLocation.resolve(fileName).normalize();

        // Security check: ensure the file is stored within the upload directory
        if (!targetLocation.startsWith(this.fileStorageLocation)) {
            throw new SecurityException("Cannot store file outside the current directory.");
        }

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("File stored successfully: {}", fileName);
            return fileName;
        } catch (IOException ex) {
            log.error("Could not store file {}. Please try again!", fileName, ex);
            throw new RuntimeException("Could not store file. Please try again!", ex);
        }
    }

    public void deleteFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            log.warn("Attempted to delete a file with a null or empty filename.");
            return;
        }

        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

        // Enhanced security check using startsWith to ensure the file is under the upload directory
        if (!filePath.startsWith(this.fileStorageLocation)) {
            log.error("Security violation: Attempted to delete file outside of upload directory: {}", fileName);
            throw new SecurityException("Cannot delete file outside upload directory");
        }

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Successfully deleted file: {}", fileName);
            } else {
                log.warn("File not found for deletion: {}", fileName);
            }
        } catch (IOException ex) {
            log.error("Error occurred while deleting file: {}", fileName, ex);
            throw new RuntimeException("Could not delete file: " + fileName, ex);
        }
    }

    public boolean fileExists(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        return Files.exists(filePath);
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

        // Validate file type: Allow only image files
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // Validate file name: Clean path and check for invalid sequences
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.contains("..")) {
            throw new IllegalArgumentException("Filename contains invalid path sequence: " + fileName);
        }
    }

    private String generateUniqueFileName(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + fileExtension;
    }

    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf(".");
        return (index == -1) ? "" : fileName.substring(index);
    }
}

