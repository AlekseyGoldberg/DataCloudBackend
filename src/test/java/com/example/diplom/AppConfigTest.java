package com.example.diplom;

import com.example.diplom.entity.File;
import com.example.diplom.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class AppConfigTest {
    private String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6NCwibG9naW4iOiJ0ZXN0IiwianRpIjoiMTZkMDU1ZGUtZjVkMi00MmYyLWE5ZjgtNTQ5Yjk3YTUxY2E0IiwiaWF0IjoxNjc3MTU2MDgwLCJleHAiOjE2NzcxNTY2ODB9.bzIT6sNI73MPXM53vk65hExWmPDvxKRxtFIoD8zbE6w";
    private String bodyForRequest = "{" +
            "\"login\" : \"test\"," +
            "\"password\" : \"test\" " +
            "}";
    private URI requestUri = new URI("http://localhost:8081/login");
    private HttpMethod method = HttpMethod.POST;

    public AppConfigTest() throws URISyntaxException {
    }

    private User user = new User("test", "98f6bcd4621d373cade4e832627b4f6");

    {
        user.setJwt(jwt);
        Set<File> files = new HashSet<>();
        files.add(new File(1L, "file1", null, 55L, new Date(), user));
        user.setFile(files);
    }

    private User someUser = new User("test1", "test1");

    private String responseUser = user.toString();

    private String responseLogin = "{\"auth-token\":\"" + jwt + "\"}";

    private RequestEntity<String> requestUser = new RequestEntity<>(this.getBodyForRequest(), this.getMethod(), this.getRequestUri());

    private RequestEntity<String> requestForEditFile = new RequestEntity<>("{\"filename\":\"editFilename\"}", HttpMethod.POST, new URI("/edit"));


}
