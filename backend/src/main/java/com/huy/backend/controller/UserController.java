package com.huy.backend.controller;

import com.huy.backend.dto.user.UpdateUserRequest;
import com.huy.backend.dto.user.UserProfileDTO;
import com.huy.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @Value("${file.upload-dir}")
    private String uploadDir;
    private final UserService userService;

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @Valid UpdateUserRequest profileDTO) {
        try {
            var updatedProfile = userService.updateProfile(profileDTO);
            return ResponseEntity.ok(updatedProfile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<Resource> getProfileImageAsResource(@PathVariable String fileName) {
        try {
            Path imagePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // or determine dynamically
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // get current user
    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

}
