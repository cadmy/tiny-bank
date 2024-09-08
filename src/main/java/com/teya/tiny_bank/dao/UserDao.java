package com.teya.tiny_bank.dao;

import com.teya.tiny_bank.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class UserDao {

    private Map<UUID, User> users = new HashMap<>();

    public User save(User user) {
        return users.put(user.getId(), user);
    }

    public User get(UUID id) {
        return users.get(id);
    }
}