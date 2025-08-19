package com.eventhub.events.controller;

import com.eventhub.events.dao.UsersDao;
import com.eventhub.events.model.Users;
import com.eventhub.events.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-auth")
public class UserAuthController {
    @Autowired
    private UsersDao usersDao;

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public Users signup(@RequestBody Users users) {
        return usersDao.createUserProfile(users);
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return authService.login(username, password);
    }
}
