package com.example.diplom.service;

import com.example.diplom.entity.File;
import com.example.diplom.entity.User;
import com.example.diplom.exception.FileExist;
import com.example.diplom.repository.FileRepository;
import com.example.diplom.repository.UserRepository;
import com.example.diplom.text.Message;
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
        User user = userRepository.getUserByLogin(principal.getName());
        File file = new File(id, filename, multipartFile.getBytes(), multipartFile.getSize(), new Date(), user);
        if (user.getFile().contains(file)) {
            throw new FileExist(Message.FILE_THIS_NAME_EXIST);
        } else {
            fileRepository.saveFile(file);
        }
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

    public void editNameFile(String filename, String newFilename) {
        File file = fileRepository.getFile(filename);
        file.setFilename(newFilename);
        fileRepository.saveFile(file);
    }

    public void deleteFile(String filename) {
        fileRepository.removeFile(filename);
    }
}
