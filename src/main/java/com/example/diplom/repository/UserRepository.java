package com.example.diplom.repository;

import com.example.diplom.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final UserJPA userJPA;

    public UserRepository(UserJPA userJPA) {
        this.userJPA = userJPA;
    }

    public User getUserById(Long id) {
        return userJPA.getUserById(id);
    }

    public User getUserByLogin(String login) {
        return userJPA.getUserByLogin(login);
    }

    public User createUser(User user) {

        return userJPA.save(user);
    }

    public void saveUser(User user) {
        userJPA.save(user);
    }

    public List<User> findAll() {
        return userJPA.findAll();
    }
}
