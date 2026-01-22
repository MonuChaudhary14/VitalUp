package org.vitalup.vitalup.service.Interface;

import java.io.InputStream;
import java.time.Duration;

public interface S3Interface {

    void uploadFile(String key, InputStream inputStream, long contentLength);

    void deleteFile(String key);

    String generatePresignedUploadUrl(String key, Duration duration);

    String generatePresignedDownloadUrl(String key, Duration duration);

    String generateFileKey(String originalFilename);

    InputStream getFileStream(String key);

    String getContentType(String key);
}
