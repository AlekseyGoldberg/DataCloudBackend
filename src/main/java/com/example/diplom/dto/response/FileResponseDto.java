package com.example.diplom.dto.response;

import com.example.diplom.entity.File;
import lombok.Data;

@Data
public class FileResponseDto {
    private final String filename;
    private final Long size;

    public FileResponseDto(File file) {
        this.filename = file.getFilename();
        this.size = file.getSize();
    }
}
