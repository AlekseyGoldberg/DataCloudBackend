package com.example.diplom;

import com.example.diplom.controller.FileController;
import com.example.diplom.controller.UserController;
import com.example.diplom.entity.File;
import com.example.diplom.exception.FileExist;
import com.example.diplom.repository.FileRepository;
import com.example.diplom.repository.UserRepository;
import com.example.diplom.text.Message;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class DiplomApplicationTests {
    private final AppConfigTest configTest = new AppConfigTest();
    @MockBean
    private FileRepository fileRepository;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserController userController;
    @Autowired
    private FileController fileController;

    DiplomApplicationTests() throws URISyntaxException {
    }


    @BeforeEach
    public void setUp() {
        when(userRepository.createUser(any())).thenReturn(configTest.getUser());
        when(userRepository.getUserByLogin(any())).thenReturn(configTest.getUser());
    }

    @Test
    public void createUserSuccessTest() throws JSONException {
        RequestEntity<String> request = new RequestEntity<>(configTest.getBodyForRequest(), configTest.getMethod(), configTest.getRequestUri());
        String actualResponse = userController.createUser(request);
        assertEquals(configTest.getResponseUser(), actualResponse);
    }

    @Test
    public void loginSuccessTest() {
        RequestEntity<String> request = configTest.getRequestUser();
        ResponseEntity<String> actualResponse = userController.login(request);
        System.out.println(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }

    @Test
    public void loginNotFoundTest() {
        when(userRepository.getUserByLogin(any())).thenReturn(configTest.getSomeUser());
        RequestEntity<String> request = configTest.getRequestUser();
        ResponseEntity<String> actualResponse = userController.login(request);
        System.out.println(actualResponse);
        assertEquals(HttpStatus.UNAUTHORIZED, actualResponse.getStatusCode());
    }

    @Test
    public void loginExceptionTest() {
        when(userRepository.getUserByLogin(any())).thenThrow(new RuntimeException("Error"));
        RequestEntity<String> request = configTest.getRequestUser();
        ResponseEntity<String> actualResponse = userController.login(request);
        System.out.println(actualResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
    }

//    @Test
//    public void deactivateToken(){
//        RequestEntity<String> request=configTest.getRequest();
//        ResponseEntity<String> actualResponse=userController.deactivateToken(request);
//    }

    @Test
    public void uploadFileSuccessTest() {
        MultipartFile file = mock(MultipartFile.class);
        Principal principal = mock(Principal.class);
        ResponseEntity<String> actualResponse = fileController.uploadFile("file", file, principal);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }

    @Test
    public void uploadFileExistTest() {
        when(fileRepository.count()).thenThrow(new FileExist(Message.FILE_THIS_NAME_EXIST));
        MultipartFile file = mock(MultipartFile.class);
        Principal principal = mock(Principal.class);
        ResponseEntity<String> actualResponse = fileController.uploadFile("file", file, principal);
        System.out.println(actualResponse);
        assertEquals(HttpStatus.EXPECTATION_FAILED, actualResponse.getStatusCode());
        assertEquals(actualResponse.getBody(), Message.FILE_THIS_NAME_EXIST);
    }

    @Test
    public void uploadFileExceptionTest() {
        when(fileRepository.count()).thenThrow(new RuntimeException());
        MultipartFile file = mock(MultipartFile.class);
        Principal principal = mock(Principal.class);
        ResponseEntity<String> actualResponse = fileController.uploadFile("file1", file, principal);
        System.out.println(actualResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
    }

    @Test
    public void deleteFileSuccessesTest() {
        Principal principal = mock(Principal.class);
        ResponseEntity<String> actualResponse = fileController.deleteFile("file1", principal);
        System.out.println(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }

    @Test
    public void deleteFileException() {
        when(userRepository.getUserByLogin(any())).thenThrow(new RuntimeException());
        Principal principal = mock(Principal.class);
        ResponseEntity<String> actualResponse = fileController.deleteFile("file1", principal);
        System.out.println(actualResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
    }

    @Test
    public void getFileSuccessesTest() {
        Principal principal = mock(Principal.class);
        ResponseEntity<byte[]> actualResponse = fileController.getFile("file1", principal);
        System.out.println(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }

    @Test
    public void getFileExceptionTest() {
        when(userRepository.getUserByLogin(any())).thenThrow(new RuntimeException());
        Principal principal = mock(Principal.class);
        ResponseEntity<byte[]> actualResponse = fileController.getFile("file1", principal);
        System.out.println(actualResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
    }

    @Test
    public void editFileSuccessesTest() {
        Principal principal = mock(Principal.class);
        ResponseEntity<String> actualRequest = fileController.editFile("file1", configTest.getRequestForEditFile(), principal);
        System.out.println(actualRequest);
        assertEquals(HttpStatus.OK, actualRequest.getStatusCode());
    }

    @Test
    public void editFileExceptionTest() {
        when(userRepository.getUserByLogin(any())).thenThrow(new RuntimeException());
        Principal principal = mock(Principal.class);
        ResponseEntity<String> actualRequest = fileController.editFile("file1", configTest.getRequestForEditFile(), principal);
        System.out.println(actualRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualRequest.getStatusCode());
    }

    @Test
    public void getFileList() {
        Principal principal = mock(Principal.class);
        ResponseEntity<Set<File>> actualResponse = fileController.getFileList(principal);
        System.out.println(actualResponse);
        assertEquals(1, actualResponse.getBody().size());
    }

    @Test
    public void getFileException() {
        when(userRepository.getUserByLogin(any())).thenThrow(new RuntimeException());
        Principal principal = mock(Principal.class);
        ResponseEntity<Set<File>> actualResponse = fileController.getFileList(principal);
        System.out.println(actualResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
    }
}
