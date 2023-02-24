package com.example.diplom.service;

import com.example.diplom.entity.User;
import com.example.diplom.exception.NotFoundException;
import com.example.diplom.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static java.time.Instant.now;

@Service
public class UserService {
    private final String secret = "U2VjcmV0X2tleV9mb3JfZ2VuZXJhdGVfSldUX3Rva2Vu";
    private final Key hmac = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public String getAuthToken(String login, String password) throws NotFoundException {
        User userFromDB = repository.getUserByLogin(login);
        String hashPassword = hashPassword(password);
        User currentUser = new User(login, hashPassword);
        if (currentUser.equals(userFromDB)) {
            String jwt = createAuthToken(userFromDB);
            userFromDB.setJwt(jwt);
            repository.saveUser(userFromDB);
            return jwt;
        } else {
            throw new NotFoundException("Неверно введенные данные");
        }
    }

    public User getUserById(Long id) {
        return repository.getUserById(id);
    }

    public User createUser(String login, String hashPassword) {
        User user = new User(login, hashPassword);
        return repository.createUser(user);
    }

    public String createAuthToken(User user) {
        return Jwts.builder()
                .claim("id", user.getId())
                .claim("login", user.getLogin())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now()))
                .setExpiration(Date.from(now().plus(10, ChronoUnit.MINUTES)))
                .signWith(hmac)
                .compact();
    }

    public Jws<Claims> parseJWT(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(hmac)
                .build()
                .parseClaimsJws(jwt);
    }

    public Long getIdFromJWT(String jwt) {
        jwt = jwt.replace("Bearer ", "");
        Jws<Claims> jws = parseJWT(jwt);
        Claims claims = jws.getBody();
        Integer id = (Integer) claims.get("id");
        return Long.valueOf(id);
    }

    public boolean checkJwt(String jwt) {
        try {
            Long id = getIdFromJWT(jwt);
            User user = repository.getUserById(id);
            String jwtWithoutBearer = jwt.replace("Bearer ", "");
            return user.getJwt().equals(jwtWithoutBearer);
        } catch (Exception e) {
            return false;
        }
    }

    public String hashPassword(String password) {
        String hashPassword = null;
        if (null == password) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes(), 0, password.length());
            hashPassword = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashPassword;
    }

    public void deactivateToken(HttpServletRequest request) {
        User user = repository.getUserById(getIdFromJWT(request.getHeader("auth-token")));
        user.setJwt("");
        repository.saveUser(user);
    }
}
