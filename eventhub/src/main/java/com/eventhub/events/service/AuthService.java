package com.eventhub.events.service;

import com.eventhub.events.dao.UsersDao;
import com.eventhub.events.model.Users;
import com.eventhub.events.utils.JwtUtil;
import com.eventhub.events.utils.PasswordHash;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsersDao usersDao;
    private final JwtUtil jwtUtil;

    public AuthService(UsersDao usersDao, JwtUtil jwtUtil) {
        this.usersDao = usersDao;
        this.jwtUtil = jwtUtil;
    }

    public String login(String username, String password) {

        Users user = usersDao.findByName(username);

        if (user == null) {
            throw new RuntimeException("Invalid username or password.");
        }

        if (!PasswordHash.verifyPassword(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password.");
        }

        return jwtUtil.generateToken(user);
    }
}