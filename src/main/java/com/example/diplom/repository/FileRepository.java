package com.example.diplom.repository;

import com.example.diplom.entity.File;
import org.springframework.stereotype.Repository;

@Repository
public class FileRepository {
    private final FileJPA fileJPA;

    public FileRepository(FileJPA fileJPA) {
        this.fileJPA = fileJPA;
    }

    public Long count() {
        return fileJPA.count();
    }

    public void saveFile(File file) {
        fileJPA.save(file);
    }

    public void removeFile(String filename) {
        fileJPA.deleteByFilename(filename);
    }

    public File getFile(String filename) {
        return fileJPA.findFileByFilename(filename);
    }
}
