package com.example.diplom.controller;

import com.example.diplom.exception.NotFoundException;
import com.example.diplom.service.UserService;
import com.example.diplom.text.Message;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(RequestEntity<String> request) {
        try {
            JSONObject body = new JSONObject(request.getBody());
            String login = body.getString("login");
            String password = body.getString("password");
            String jwt = userService.getAuthToken(login, password);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("auth-token", jwt);
            return new ResponseEntity<>(jsonBody.toString(), HttpStatus.OK);
        } catch (NotFoundException e) {
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(Message.INTERNAL_SERVICE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public String createUser(RequestEntity<String> request) throws JSONException {
        JSONObject body = new JSONObject(request.getBody());
        String login = body.getString("login");
        String hashPassword = userService.hashPassword(body.getString("password"));
        return userService.createUser(login, hashPassword).toString();
    }
}
