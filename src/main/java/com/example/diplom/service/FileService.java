package com.example.diplom.service;

import com.example.diplom.entity.File;
import com.example.diplom.entity.User;
import com.example.diplom.repository.FileRepository;
import com.example.diplom.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.Set;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public FileService(FileRepository fileRepository, UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }


    public void saveFile(String filename, MultipartFile multipartFile, Principal principal) throws IOException {
        Long id = fileRepository.count() + 1;
        File file = new File(id, filename, multipartFile.getBytes(), multipartFile.getSize(), new Date());
        User user = userRepository.getUserByLogin(principal.getName());
        user.getFile().add(file);
        userRepository.saveUser(user);
    }

    public File getFile(String filename, Principal principal) {
        File returnFile = null;
        User user = userRepository.getUserByLogin(principal.getName());
        Set<File> listOfFile = user.getFile();
        for (File file : listOfFile) {
            if (file.getFilename().equals(filename))
                returnFile = file;
        }
        return returnFile;
    }

    public Set<File> getFileList(Principal principal) {
        User user = userRepository.getUserByLogin(principal.getName());
        return user.getFile();
    }

    public void editNameFile(String filename, String newFilename, Principal principal) {
        User user = userRepository.getUserByLogin(principal.getName());
        Set<File> fileList = user.getFile();
        for (File file : fileList) {
            if (file.getFilename().equals(filename)) {
                file.setFilename(newFilename);
            }
        }
        userRepository.saveUser(user);
    }

    public void deleteFile(String filename, Principal principal) {
        User user = userRepository.getUserByLogin(principal.getName());
        Set<File> fileList = user.getFile();
        fileList.removeIf(file -> file.getFilename().equals(filename));
        userRepository.saveUser(user);
    }
}
