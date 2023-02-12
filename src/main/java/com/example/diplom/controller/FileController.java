package com.example.diplom.controller;

import com.example.diplom.entity.File;
import com.example.diplom.service.FileService;
import com.example.diplom.text.Message;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Set;

@RestController
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestParam String filename, @RequestPart MultipartFile file, Principal user) {
        try {
            fileService.saveFile(filename, file, user);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e);
            return new ResponseEntity<>(Message.FILE_THIS_NAME_EXIST, HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(Message.INTERNAL_SERVICE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/file")
    public ResponseEntity<String> deleteFile(@RequestParam String filename, Principal principal, RequestEntity<String> request) {
        try {
            fileService.deleteFile(filename, principal);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(Message.INTERNAL_SERVICE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> getFile(@RequestParam String filename, Principal principal) {
        try {
            File file = fileService.getFile(filename, principal);
            return new ResponseEntity<>(file.getFile(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/file")
    public ResponseEntity<String> editFile(@RequestParam String filename, RequestEntity<String> request, Principal principal) {
        try {
            JSONObject body = new JSONObject(request.getBody());
            fileService.editNameFile(filename, body.getString("filename"), principal);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public Set<File> getFileList(Principal principal) {
        try {
            return fileService.getFileList(principal);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }
}
