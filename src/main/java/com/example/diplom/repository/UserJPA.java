package com.example.diplom.repository;

import com.example.diplom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserJPA extends JpaRepository<User, Long> {
    User getUserByLoginAndHashPassword(String login, String hashPassword);

    User getUserByLogin(String login);

    User getUserById(Long id);


}
