package org.vitalup.vitalup.controller.FilesSharing;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vitalup.vitalup.dto.ApiResponse;
import org.vitalup.vitalup.dto.FilesSharing.PresignedRequestDTO;
import org.vitalup.vitalup.service.Interface.S3Interface;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileSharingController {

    private final S3Interface s3Service;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String key = s3Service.generateFileKey(file.getOriginalFilename());
        s3Service.uploadFile(key, file.getInputStream(), file.getSize());
        return ResponseEntity.ok(new ApiResponse<>(200, "File uploaded successfully", key));
    }

    @PostMapping("/presigned/upload")
    public ResponseEntity<ApiResponse<String>> getPresignedUploadUrl(@RequestBody PresignedRequestDTO request) {
        String key = request.getFile();
        Duration duration = Duration.ofMinutes(10);
        String url = s3Service.generatePresignedUploadUrl(key, duration);
        return ResponseEntity.ok(new ApiResponse<>(200, "Presigned upload URL generated", url));
    }

    @GetMapping("/download")
    public ResponseEntity<ApiResponse<String>> getPresignedDownloadUrl(@RequestParam String key) {
        String url = s3Service.generatePresignedDownloadUrl(key, Duration.ofMinutes(10));
        return ResponseEntity.ok(new ApiResponse<>(200, "Presigned download URL generated successfully", url));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteFile(@RequestParam String key) {
        s3Service.deleteFile(key);
        return ResponseEntity.ok(new ApiResponse<>(200, "File deleted successfully", key));
    }

}
