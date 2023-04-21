package com.example.diplom.controller;

import com.example.diplom.dto.response.FileResponseDto;
import com.example.diplom.entity.File;
import com.example.diplom.exception.FileExist;
import com.example.diplom.service.FileService;
import com.example.diplom.text.Message;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Log4j2
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestParam String filename, @RequestPart MultipartFile file, Principal user) {
        try {
            log.info("Accept request for save file");
            fileService.saveFile(filename, file, user);
            log.info("File was saved");
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (FileExist e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(Message.INTERNAL_SERVICE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/file")
    public ResponseEntity<String> deleteFile(@RequestParam String filename) {
        try {
            log.info("Accept request for delete file");
            fileService.deleteFile(filename);
            log.info("File was deleted");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(Message.INTERNAL_SERVICE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> getFile(@RequestParam String filename, Principal principal) {
        try {
            log.info("Accept request for get file");
            File file = fileService.getFile(filename, principal);
            log.info("File with size: {} was sent", file.getSize());
            return new ResponseEntity<>(file.getFile(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/file")
    public ResponseEntity<String> editFile(@RequestParam String filename, RequestEntity<String> request) {
        try {
            log.info("Accept request for edit file");
            JSONObject body = new JSONObject(request.getBody());
            fileService.editNameFile(filename, body.getString("filename"));
            log.info("file was edited on {}", filename);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileResponseDto>> getFileList(Principal principal) {
        try {
            log.info("Accept request for list of files");
            Set<File> files = fileService.getFileList(principal);
            List<FileResponseDto> result = files.stream()
                    .map(FileResponseDto::new)
                    .collect(Collectors.toList());
            log.info("List size: {}", result.size());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
