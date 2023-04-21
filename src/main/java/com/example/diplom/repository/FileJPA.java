package com.example.diplom.repository;

import com.example.diplom.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface FileJPA extends JpaRepository<File, Long> {
    @Transactional
    void deleteByFilename(String filename);

    File findFileByFilename(String filename);
}
