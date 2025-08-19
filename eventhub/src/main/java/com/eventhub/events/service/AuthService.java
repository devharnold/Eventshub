package com.eventhub.events.service;

import com.eventhub.events.dao.UsersDao;
import com.eventhub.events.model.Users;
import com.eventhub.events.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String username, String password) {
        Users user = usersDao.findByName(username);
        if (user != null && user.getPassword().equals(password)) {
            return jwtUtil.generateToken(user);
        }
        throw new RuntimeException("Invalid User Credentials");
    }
}
