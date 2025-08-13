package com.api.redis.controller;

import com.api.redis.dao.UserDao;
import com.api.redis.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDao userDao;

    // Create new user with generated UUID
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setUserId(UUID.randomUUID().toString());
        return userDao.save(user);
    }

    // Get user by ID
    @GetMapping("/{userId}")
    public User getUser(@PathVariable String userId) {
        return userDao.get(userId);
    }

    // Get all users
    @GetMapping
    public Map<Object, Object> getAllUsers() {
        return userDao.findAll();
    }

    // Delete user by ID
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userDao.delete(userId);
    }
}
