package com.api.redis.dao;

import com.api.redis.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserDao {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String KEY = "USER1234";

    // Save a user
    public User save(User user) {
        redisTemplate.opsForHash().put(KEY, user.getUserId(), user);
        return user;
    }

    // Get a user by ID
    public User get(String userId) {
        return (User) redisTemplate.opsForHash().get(KEY, userId);
    }

    // Get all users
    public Map<Object, Object> findAll() {
        return redisTemplate.opsForHash().entries(KEY);
    }

    // Delete a user by ID
    public void delete(String userId) {
        redisTemplate.opsForHash().delete(KEY, userId);
    }
}
