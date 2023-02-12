package com.example.diplom.repository;

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
}
