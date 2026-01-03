package org.vitalup.vitalup.dto.FilesSharing;

import lombok.Data;

@Data
public class PresignedRequestDTO {

    private String file;
    private String contentType;

}
